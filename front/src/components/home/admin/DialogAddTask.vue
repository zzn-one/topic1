<template>
    <el-dialog title="新增选题任务" center :visible.sync="dialogVisible" width="600px" @close="closeDialog">
        <div class="form_div">
            <el-form :model="taskForm" label-position="left" label-width="120px" :rules="rules" ref="addTaskForm">
                <el-form-item label="任务名称" prop="name">
                    <el-input v-model="taskForm.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="届数" prop="year">
                    <el-date-picker v-model="taskForm.year" type="year" placeholder="选择届数">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="学院" prop="college">
                    <el-select v-model="taskForm.college" placeholder="请选择学院">
                        <el-option v-for="item in collegeList" :label="item.description"
                            :value="item.college_id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="专业" prop="major">
                    <el-select v-model="taskForm.major" placeholder="请选择专业">
                        <el-option v-for="item in majorList" :label="item.description"
                            :value="item.major_id"></el-option>
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
            <el-button plain type="danger" class="dialog_footer_btn"  @click="closeDialog">取 消</el-button>
            <el-button plain type="primary" class="dialog_footer_btn"  @click="addTask">提 交</el-button>
        </div>
    </el-dialog>
</template>
<script>
export default {
    name: "DialogAddTask",
    props: ['visible'],
    emits: ['close'],
    data() {
        return {
            dialogVisible: false,
            collegeList: [],
            majorList: [],
            taskForm: {
                name: '',
                year: '',
                college: '',
                major: '',
                startTime: '',
                duration: '',
                publishBy: ''
            },
            rules: {
                name: [
                    { required: true, message: '请输入任务名称', trigger: 'blur' },
                ],
                year: [
                    { required: true, message: '请选择毕业届数', trigger: 'change' }
                ],
                startTime: [
                    { required: true, message: '请选择任务开始时间', trigger: 'change' }
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
        addTask() {
            this.$refs['addTaskForm'].validate((valid) => {
                if (valid) {
                    //验证通过
                    this.closeDialog()
                } else {
                    //验证不通过
                    return false
                }
            })


        },

        closeDialog() {
            this.dialogVisible = false
            this.$emit('close', false)
        }

    },
    created() {
        //发送请求获取collegeList
        this.collegeList = [
            { college_id: '01', description: '信息与通信工程学院' },
            { college_id: '02', description: '土木学院' },
            { college_id: '03', description: '艺术学院' },
        ]
    },
    watch: {
        'taskForm.college'(newVal) {
            console.log(newVal);
            //根据college获取majorList
            this.majorList = [
                { major_id: '0101', description: '计算机科学与技术' },
                { major_id: '0102', description: '软件工程' },
                { major_id: '0103', description: '通信工程' },
            ]
        },
        visible(val) {
            this.dialogVisible = val
        }
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
.dialog_footer{
    margin-bottom: 20px;
    margin-top: -20px;
}

/deep/.el-form-item {
    margin-left: 60px;
}
</style>
