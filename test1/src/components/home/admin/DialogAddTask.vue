<template>
    <el-dialog title="发布选题任务" center :visible.sync="dialogVisible" width="600px" @close="closeDialog">
        <div class="form_div">
            <el-form :model="taskForm" label-position="left" label-width="120px" :rules="rules" ref="addTaskForm">
                <el-form-item label="任务名称" prop="name">
                    <el-input v-model="taskForm.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="届数" prop="year">
                    <el-input v-model="taskForm.year" autocomplete="off" disabled></el-input>
                </el-form-item>

                <el-form-item label="专业" prop="major">
                    <el-select v-model="taskForm.major" placeholder="请选择专业">
                        <el-option v-for="item in majorList" :label="item.description" :key="item.major"
                            :value="item.major"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="任务开始时间" prop="startTime">
                    <el-date-picker v-model="taskForm.startTime" type="datetime" placeholder="选择任务开始时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="选课时长(分钟)" prop="duration">
                    <el-input v-model.number="taskForm.duration" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
        </div>
        <div slot="footer" class="dialog_footer">
            <el-button plain type="primary" class="dialog_footer_btn" @click="publish">发 布</el-button>
            <el-button plain type="danger" class="dialog_footer_btn" @click="closeDialog">取 消</el-button>

        </div>
    </el-dialog>
</template>
<script>
import jwt_prase from '@/assets/js/jwtPrase'

export default {
    name: "DialogAddTask",
    props: ['visible'],
    emits: ['close'],
    data() {
        var validateStartTime = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请选择开始日期'));
            } else if (value <= new Date()) {
                callback(new Error('开始日期不能为过去的时间！'));
            } else {
                callback();
            }
        };
        return {
            dialogVisible: false,
            majorList: [],
            taskForm: {
                name: '',
                year: '',
                college: '',
                major: '',
                startTime: '',
                duration: '',
                publishBy: '',
                publishTime: '',
                publishName: ''
            },
            rules: {
                name: [
                    { required: true, message: '请输入任务名称', trigger: 'blur' },
                ],
                year: [
                    { required: true, message: '请选择毕业届数', trigger: 'change' }
                ],
                startTime: [
                    { validator: validateStartTime, trigger: 'change' }
                ],
                duration: [
                    { required: true, message: '请填写选课时长', trigger: 'blur' }
                ],
                college: [
                    { required: true, message: '请选择学院', trigger: 'change' }
                ],
                major: [
                    { required: true, message: '请选择专业', trigger: 'change' }
                ],

            }
        }
    },
    methods: {
        //新增任务
        publish() {
            this.$refs['addTaskForm'].validate((valid) => {
                if (valid) {
                    //验证通过
                    this.addTask()

                } else {
                    //验证不通过
                    return false
                }
            })
        },

        async addTask() {
            this.taskForm.publishBy = jwt_prase().id
            this.taskForm.publishName = jwt_prase().name
            this.taskForm.publishTime = new Date().toJSON()
            //处理date类型的数据
            if (typeof (this.taskForm.startTime) === 'object') {
                this.taskForm.startTime = this.taskForm.startTime.toJSON()
            }

            const resp = await this.$http(
                {
                    method: "post",
                    url: '/task',
                    data: this.taskForm
                }
            )

            if (resp.data.code === 200) {
                if (resp.data.data === true) {

                    this.$message({
                        message: resp.data.msg,
                        type: "success"
                    })
                    this.closeDialog()
                } else {
                    this.$message({
                        message: resp.data.msg,
                        type: "error"
                    })
                }
            }

        },

        //发送请求获取majorList
        async getMajorList() {
            this.taskForm.college = jwt_prase().college
            this.taskForm.year = localStorage.getItem("year")
            const resp = await this.$http.get('/dict/major/college/' + jwt_prase().college)
            if (resp.data.code === 200) {
                this.majorList = resp.data.data
            }
        },

        closeDialog() {
            this.dialogVisible = false
            this.$emit('close', false)
        }

    },
    async created() {
        this.getMajorList()
    },
    watch: {
        visible(val) {
            this.dialogVisible = val
        },

    }
}
</script>
<style scoped lang="less">
/deep/.el-input__inner {
    width: 300px;
}

.dialog_footer_btn {
    margin-left: 42px;
}

.dialog_footer {
    margin-bottom: 20px;
    margin-top: -20px;
}

/deep/.el-form-item {
    margin-left: 60px;
}
</style>
