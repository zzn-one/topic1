<template>
    <el-dialog :title="title" :visible.sync="visible" width="800px" @close="closeDialogEmit">

        <el-form ref="infoForm" :rules="rules" :model="infoDetail" label-width="100px" class="box-form">
            <el-form-item label="学号" prop="sid">
                <el-input v-model.trim="infoDetail.sid"></el-input>
            </el-form-item>
            <el-form-item label="学生姓名" prop="sname">
                <el-input v-model.trim="infoDetail.sname"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
                <el-select v-model.trim="infoDetail.gender" placeholder="请选择性别">
                    <el-option v-for="item in genderList" :key="item.gender" :label="item.description"
                        :value="item.gender">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model.trim="infoDetail.password"></el-input>
            </el-form-item>
            <el-form-item label="毕业年份" prop="graduationYear">
                <el-input v-model.trim="infoDetail.graduationYear"></el-input>
            </el-form-item>
            <el-form-item label="学院" prop="college">
                <el-select v-model.trim="infoDetail.college" placeholder="请选择学院">
                    <el-option v-for="item in collegeList" :key="item.college" :label="item.description"
                        :value="item.college">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="专业" prop="major">
                <el-select v-model.trim="infoDetail.major" placeholder="请选择专业">
                    <el-option v-for="item in majorList" :key="item.major" :label="item.description"
                        :value="item.major">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model.trim="infoDetail.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model.trim="infoDetail.email"></el-input>
            </el-form-item>
            <el-form-item label="课题名称" v-if="formType === '1'">
                <el-input disabled v-model.trim="infoDetail.topicName"></el-input>
            </el-form-item>
            <el-form-item label="课题名称" v-else>
                <el-input v-model.trim="infoDetail.topicName"></el-input>
            </el-form-item>

            <el-form-item>
                <el-row>
                    <el-button plain type="primary" class="info_btns" v-if="formType === '0'"
                        @click="submitForm('infoForm')">新增学生信息
                    </el-button>

                    <el-button plain type="primary" class="info_btns" v-else @click="submitForm('infoForm')">修改学生信息
                    </el-button>

                    <el-button plain type="danger" class="info_btns" @click="closeDialogEmit">取消</el-button>

                </el-row>


            </el-form-item>
        </el-form>
    </el-dialog>

</template>

<script>
import jwtPrase from '@/assets/js/jwtPrase';
export default {
    name: "DialogStuInfo",
    props: ["formType", "sId", 'dialogVisible'],
    emits: ['closeDialog'],
    data() {
        return {
            visible: false,
            title: '',
            infoDetail: {
                sid: '',
                sname: "",
                gender: "",
                password: "",
                graduationYear: "",
                college: "",
                major: "",
                phone: "",
                email: "",
                topicName: ''
            },
            genderList: '',
            collegeList: '',
            majorList: '',

            rules: {
                sid: [
                    { required: true, message: '学号不能为空', trigger: 'blur' },
                ],
                sname: [
                    { required: true, message: '学生姓名不能为空', trigger: 'blur' },
                ],
                gender: [
                    { required: true, message: '请选择学生性别', trigger: 'change' },
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' },
                ],
                graduationYear: [
                    { required: true, message: '毕业年份不能为空', trigger: 'blur' },
                ],
                college: [
                    { required: true, message: '请选择学院', trigger: 'change' },
                ],
                major: [
                    { required: true, message: '请选择专业名称', trigger: 'change' },
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { mix: 11, max: 11, message: '手机号长度为11位', trigger: 'change' }
                ],
                email: [
                    { required: true, message: '请输入邮箱账号', trigger: 'blur' },
                ],

            }
        }
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    // 发送请求
                    if (this.formType == '0') {
                        //新增
                        this.$confirm('此操作将新增毕业生信息, 是否继续?', '温馨提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: "success"
                        }).then(() => {
                            // 发送新增请求
                            this.addUser()
                        }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消新增'
                            });
                        })
                    } else if (this.formType == '1') {
                        //修改
                        this.$confirm('此操作将修改毕业生信息, 是否继续?', '温馨提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: "success"
                        }).then(() => {
                            // 发送修改请求
                            this.updataUser()
                        }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消修改'
                            });
                        })

                    }
                } else {
                    return false;
                }
            });
        },
        // 关闭dialog
        closeDialogEmit() {
            this.visible = false
            this.$emit('closeDialog', this.visible)
        },
        //发送请求获取collegeList
        async getCollegeList() {
            const resp = await this.$http.get('/dict/college')
            if (resp.data.code === 200) {
                this.collegeList = resp.data.data
            }
        },
        //发送请求获取genderList
        async getGenderList() {
            const resp = await this.$http.get('/dict/gender')
            if (resp.data.code === 200) {
                this.genderList = resp.data.data
            }
        },
        //发送请求获取majorList
        async getMajorList(id) {
            const resp = await this.$http.get('/dict/major/college/' + id)
            if (resp.data.code === 200) {
                this.majorList = resp.data.data
            }
        },
        //新增毕业生信息
        async addUser() {
            //设置更新者的id
            this.infoDetail.updatedBy = jwtPrase().id

            const resp = await this.$http({
                url: '/userStudent',
                method: "post",
                data: this.infoDetail
            })
            if (resp.data.code === 200) {
                if (resp.data.data == true) {
                    this.$message.success(resp.data.msg)

                } else {
                    this.$message.error(resp.data.msg)
                }
            }
        },
        //修改毕业生信息
        async updataUser() {
            //设置更新者的id
            this.infoDetail.updatedBy = jwtPrase().id

            const resp = await this.$http({
                url: '/userStudent',
                method: "put",
                data: this.infoDetail
            })
            if (resp.data.code === 200) {
                if (resp.data.data == true) {
                    this.$message.success(resp.data.msg)
                } else {
                    this.$message.error(resp.data.msg)
                }
            }
        },
        //根据sId获取毕业生信息
        async getStudentInfo() {
            const resp = await this.$http.get("/userStudent/" + this.sId)
            if (resp.data.code === 200 && resp.data.data !== null) {
                this.infoDetail = resp.data.data
            }
        }
    },
    watch: {
        dialogVisible(newval) {
            this.visible = newval
        },
        'infoDetail.college'(newval) {
            //根据newval发送请求获取majorList
            this.getMajorList(newval)
        },
        sId(val) {
            if (val === '') {
                //重置表单
                this.infoDetail = {
                    sid: '',
                    sname: "",
                    gender: "",
                    password: "",
                    graduationYear: "",
                    college: "",
                    major: "",
                    phone: "",
                    email: "",
                    topicName: ''
                }
            }
            //发送请求获取毕业生信息
            this.getStudentInfo()
        }
    },
    updated() {

        if (this.formType === '1') {
            this.title = '修改学生信息'
        } else {
            this.title = '新增学生信息'
        }

    },
    created() {
        //发送请求获取genderList
        this.getGenderList()
        //发送请求获取collegeList
        this.getCollegeList()

    }

}
</script>
<style scoped lang="less">
.info_btns {
    margin-top: 30px;
    margin-left: 110px;
    width: 150px;
    font-size: 18px;
}

.box-form {
    line-height: 23px;
    font-size: 22px;
    text-align: left;
    height: 720px;
}
</style>