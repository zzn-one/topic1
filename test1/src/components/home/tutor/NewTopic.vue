<template>
    <div>
        <el-row class="header_main">
            <div class="title_div">毕业课题提交表</div>
        </el-row>
        <!-- 新增毕业课题 -->
        <el-form :inline="true" ref="newTopicForm" :rules="rules" :model="newTopicForm" class="form_main">

            <el-form-item label="课题名称" prop="topicName">
                <el-input v-model="newTopicForm.topicName"></el-input>
            </el-form-item>
            <el-form-item label="课题类型" prop="type">
                <el-select v-model="newTopicForm.type">
                    <el-option v-for="item in typeList" :label="item.description" :value="item.type"
                        :key="item.type"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="课题来源" prop="source">
                <el-select v-model="newTopicForm.source">
                    <el-option v-for="item in sourceList" :label="item.description" :value="item.source"
                        :key="item.source"></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <el-row>
            <el-button plain type="primary" class="btn" @click="nextTopic('newTopicForm')">提交,编辑下一个课题</el-button>
            <el-button plain type="danger" class="btn" @click="resetForm('newTopicForm')">重新输入</el-button>
            <el-button plain type="success" class="btn" @click="submitForm('newTopicForm')">提交,关闭页面</el-button>
        </el-row>


    </div>
</template>

<script>
import jwt_prase from '@/assets/js/jwtPrase';

export default {
    name: "NewTopic",
    emits: ['close'],
    data() {

        return {
            newTopicForm:
            {
                topicName: '',
                type: '',
                source: '',
                major: '',
                college: '',
                year: '',
                createdBy: '',
                createdName:'',
                createdTime: '',
                updatedBy: '',
                updatedTime: ''
            },

            typeList: [],
            sourceList: [],

            rules: {
                "topicName": [
                    { required: true, message: '请输入课题名称', trigger: 'blur' }
                ],
                "type": [
                    { required: true, message: '请选择课题类型', trigger: 'change' }
                ],
                "source": [
                    { required: true, message: '请选择课题来源', trigger: 'change' }
                ]
            }

        }
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {//验证通过
                    //发送请求，新增topic
                    this.addTopic()

                    this.resetForm(formName)
                    this.closeDialog()
                } else {
                    return false;
                }
            })
        },
        nextTopic(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {//验证通过

                    this.addTopic()

                    this.resetForm(formName)
                } else {
                    return false;
                }
            })
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        closeDialog() {
            this.$emit('close', false)
        },
        //发送请求获取typeList
        async getTypeList() {
            const resp = await this.$http.get('/dict/topicType')
            if (resp.data.code === 200) {
                this.typeList = resp.data.data
            }
        },
        //发送请求获取sourceList
        async getSourceList() {
            const resp = await this.$http.get('/dict/topicSource')
            if (resp.data.code === 200) {
                this.sourceList = resp.data.data
            }
        },
        //newTopic表单的初始化
        async initTopicForm() {

            let tId = jwt_prase().id
            this.newTopicForm.createdBy = tId
            this.newTopicForm.createdName=jwt_prase().name
            this.newTopicForm.updatedBy = tId
            this.newTopicForm.year = localStorage.getItem("year")
            this.newTopicForm.college = jwt_prase().college
            this.newTopicForm.major = jwt_prase().major
            
        },
        //新增topic
        async addTopic() {
            this.newTopicForm.updatedTime = this.newTopicForm.createdTime = new Date().toJSON()
            const resp = await this.$http({
                url: "/topic",
                method: "post",
                data: this.newTopicForm
            })

            if (resp.data.code === 200 && resp.data.data === true) {
                //新增成功
                this.$message({
                    message: "课题已提交到系统，等待管理员审核。",
                    type: "success"
                })
            } else if (resp.data.code === 200 && resp.data.data === false) {
                //失败
                this.$message({
                    message: resp.data.msg,
                    type: "error"
                })
            } else {
                this.$message({
                    message: "系统错误！",
                    type: "error"
                })
            }

        }

    },
    created() {
        //发送请求获取typeList
        this.getTypeList()
        //发送请求获取sourceList
        this.getSourceList()
        this.initTopicForm()

    }


}
</script>
<style scoped lang="less">
.form_main {
    line-height: 23px;
}

.btn {
    width: 200px;
    font-size: 18px;
}

.header_main {
    line-height: 23px;
    font-size: 18px;
    text-align: left;
    margin-bottom: 50px;

}

.title_div {
    text-align: center;
    font-size: 22px;
    font-weight: bold;
}

.header_main .el-button {
    height: 23px;
    line-height: 23px;
    font-size: 18px;
    text-align: right;
    padding: 0;

}


.el-select {
    width: 120px;
}

/deep/.el-form--inline .el-form-item__label {
    font-size: 18px;
}
</style>