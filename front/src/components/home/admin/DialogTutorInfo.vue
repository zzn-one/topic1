<template>
    <el-dialog :title="title" :visible.sync="visible" width="800px" @close="closeDialogEmit">

        <el-form ref="infoForm" :rules="rules" :model="infoDetail" label-width="100px" class="box-form">
            <el-form-item label="工号" prop="tId">
                <el-input v-model.trim="infoDetail.tId"></el-input>
            </el-form-item>
            <el-form-item label="导师姓名" prop="tName">
                <el-input v-model.trim="infoDetail.tName"></el-input>
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
                    <el-option v-for="item in collegeList" :key="item.college_id" :label="item.description"
                        :value="item.college_id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="所属系" prop="major">
                <el-select v-model.trim="infoDetail.major" placeholder="请选择系">
                    <el-option v-for="item in majorList" :key="item.major_id" :label="item.description+'系'"
                        :value="item.major_id">
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
export default {
    name: "DialogTutorInfo",
    props: ["formType", "tId", 'dialogVisible'],
    emits: ['closeDialog'],
    data() {
        return {
            visible: false,
            title: '',
            infoDetail: {
                tId: '',
                tName: "",
                title:'',
                gender: "",
                password: "",
                college: "",
                major: "",
                phone: "",
                email: "",
            },
            genderList: '',
            collegeList: '',
            majorList: '',
            titleList:'',

            rules: {
                tId: [
                    { required: true, message: '工号不能为空', trigger: 'blur' },
                ],
                tName: [
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
                    { length: 11, message: '手机号长度为11位', trigger: 'change' }
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
                    let code = 201
                    if (code === 200) {
                        //请求成功
                        this.$message.success('信息已更新！')
                    } else {
                        this.$message.error('信息更新失败！')
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
        }
    },
    watch: {
        dialogVisible(newval) {
            this.visible = newval
        },
        'infoDetail.college'(newval) {
            //根据newval发送请求获取majorList
            this.majorList=[
                { major_id: '0101', description: '计算机科学与技术' },
                { major_id: '0102', description: '软件工程' },
                { major_id: '0103', description: '通信工程' },
            ]
        }
    },
    updated() {

        if (this.formType === '1') {
            this.title = '修改导师信息'
            //使用sId发送请求，获取sId对应导师的全部信息
            console.log("请求tId对应导师的信息" + this.sId);
        } else {
            this.title = '新增导师信息'

        }

    },
    created() {
        //发送请求获取genderList
        this.genderList = [
            { gender: 0, description: '未知' },
            { gender: 1, description: '男' },
            { gender: 2, description: '女' },
        ]
        //发送请求获取collegeList
        this.collegeList = [
            { college_id: '01', description: '信息与通信工程学院' },
            { college_id: '02', description: '土木学院' },
            { college_id: '03', description: '艺术学院' },
        ]
        //发送请求获取titleList
        this.titleList=[
            {title:0,description:"讲师"},
            {title:0,description:"副教授"},
            {title:0,description:"教授"},

        ]
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