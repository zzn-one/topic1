<template>
    <el-dialog :title="title" :visible.sync="visible" width="800px" @close="closeDialogEmit">

        <el-form ref="infoForm" :rules="rules" :model="infoDetail" label-width="120px" class="box-form">
            <el-form-item label="工号" prop="tid">
                <el-input v-model.trim="infoDetail.tid"></el-input>
            </el-form-item>
            <el-form-item label="导师姓名" prop="tutorName">
                <el-input v-model.trim="infoDetail.tutorName"></el-input>
            </el-form-item>
            <el-form-item label="导师职称" prop="title">
                <el-select v-model.trim="infoDetail.title" placeholder="请选择导师职称">
                    <el-option v-for="item in titleList" :key="item.title" :label="item.description"
                        :value="item.title">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
                <el-select v-model.trim="infoDetail.gender" placeholder="请选择性别">
                    <el-option v-for="item in genderList" :key="item.gender" :label="item.description"
                        :value="item.gender">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="所属学院" prop="college">
                <el-select v-model.trim="infoDetail.college" placeholder="请选择学院">
                    <el-option v-for="item in collegeList" :key="item.college" :label="item.description"
                        :value="item.college">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="所属系" prop="major">
                <el-select v-model.trim="infoDetail.major" placeholder="请选择系">
                    <el-option v-for="item in majorList" :key="item.major" :label="item.description + '系'"
                        :value="item.major">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model.trim="infoDetail.password"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model.trim="infoDetail.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model.trim="infoDetail.email"></el-input>
            </el-form-item>
            <el-form-item label="导师办公室" prop="office">
                <el-input v-model.trim="infoDetail.office"></el-input>
            </el-form-item>


            <el-form-item>
                <el-row>
                    <el-button plain type="primary" class="info_btns" v-if="formType === '0'"
                        @click="submitForm('infoForm')">新增导师信息
                    </el-button>

                    <el-button plain type="primary" class="info_btns" v-else @click="submitForm('infoForm')">修改导师信息
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
    name: "DialogTutorInfo",
    props: ["formType", "tId", 'dialogVisible'],
    emits: ['closeDialog'],
    data() {
        return {
            visible: false,
            title: '',
            infoDetail: {
                id: '',
                tid: '',
                tutorName: "",
                title: '',
                gender: "",
                password: "",
                college: "",
                major: "",
                phone: "",
                email: "",
                office: "",
                updatedBy: ""
            },
            genderList: [],
            collegeList: [],
            majorList: [],
            titleList: [],

            rules: {
                tid: [
                    { required: true, message: '工号不能为空', trigger: 'blur' },
                ],
                tutorName: [
                    { required: true, message: '导师姓名不能为空', trigger: 'blur' },
                ],
                title: [
                    { required: true, message: '请选择导师职称', trigger: 'change' },
                ],
                gender: [
                    { required: true, message: '请选择导师性别', trigger: 'change' },
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' },
                ],
                college: [
                    { required: true, message: '请选择学院', trigger: 'change' },
                ],
                major: [
                    { required: true, message: '请选择专业名称', trigger: 'change' },
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { min: 11, max: 11, message: '手机号长度为11位', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱账号', trigger: 'blur' },
                ],
                office: [
                    { required: true, message: '请输入导师办公室', trigger: 'blur' },
                ],

            }
        }
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.formType == '0') {
                        this.$confirm('此操作将新增导师信息, 是否继续?', '温馨提示', {
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
                        this.$confirm('此操作将修改导师信息, 是否继续?', '温馨提示', {
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
        //发送请求获取titleList
        async getTitleList() {
            const resp = await this.$http.get('/dict/tutorTitle')
            if (resp.data.code === 200) {
                this.titleList = resp.data.data
            }
        },
        //发送请求 新增导师
        async addUser() {

            //设置更新者的id
            this.infoDetail.updatedBy = jwtPrase().id

            const resp = await this.$http({
                url: '/userTutor',
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
        async getUserByTid() {
            const resp = await this.$http.get("/userTutor/" + this.tId)

            if (resp.data.code === 200) {
                this.infoDetail = resp.data.data
            }
        },
        async updataUser() {
            //设置更新者的id
            this.infoDetail.updatedBy = jwtPrase().id

            const resp = await this.$http({
                url: '/userTutor',
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
        }
    },
    watch: {
        dialogVisible(newval) {
            this.visible = newval
        },
        'infoDetail.college'(newval) {
            if (newval == '') {
                return;
            }
            //根据newval发送请求获取majorList
            this.getMajorList(newval)
        },
        tId(newval) {
            //发送请求 获取用户原信息
            if (newval === '') {
                return;
            }
            this.getUserByTid()
        },
        formType(newval) {
            if (newval === '1') {
                this.title = '修改导师信息'

            } else {
                this.title = '新增导师信息'
                //重置表单
                this.infoDetail = {
                    id: '',
                    tid: '',
                    tutorName: "",
                    title: '',
                    gender: "",
                    password: "",
                    college: "",
                    major: "",
                    phone: "",
                    email: "",
                    office: "",
                    updatedBy: ""
                }

            }
        }
    },

    created() {
        //发送请求获取genderList
        this.getGenderList()
        //发送请求获取collegeList
        this.getCollegeList()
        //发送请求获取titleList
        this.getTitleList()


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