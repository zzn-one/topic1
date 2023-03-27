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
            <el-form-item label="课题类型" prop="topicType">
                <el-select v-model="newTopicForm.topicType">
                    <el-option v-for="item in typeList" :label="item.description" :value="item.type"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="课题来源" prop="topicSource">
                <el-select v-model="newTopicForm.topicSource">
                    <el-option v-for="item in sourceList" :label="item.description" :value="item.source"></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <el-row>
            <el-button plain type="primary" class="btn" @click="submitForm('newTopicForm')">提交,编辑下一个课题</el-button>
            <el-button plain type="danger" class="btn" @click="resetForm('newTopicForm')">重新输入</el-button>
            <el-button plain type="success" class="btn" @click="submitForm('newTopicForm')">提交,关闭页面</el-button>
        </el-row>


    </div>
</template>

<script>
export default {
    name: "NewTopic",
    data() {
        var validateTopicName = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入课题名称'));
            } else {
                callback();
            }
        };
        return {
            newTopicForm:
            {
                topicName: '',
                topicType: '',
                topicSource: ''
            },

            typeList: [],
            sourceList: [],

            rules: {
                "topicName": [
                    { required: true, validator: validateTopicName, trigger: 'blur' }
                ],
                "topicType": [
                    { required: true, message: '请选择课题类型', trigger: 'change' }
                ],
                "topicSource": [
                    { required: true, message: '请选择课题来源', trigger: 'change' }
                ]
            }

        }
    },
    methods: {
        submitForm(formName) {
            const _this = this
            this.$refs[formName].validate((valid) => {
                if (valid) {//验证通过
                    alert('submit!');
                    this.$refs[formName].resetFields();
                } else {
                    return false;
                }
            })
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();

        },

    },
    created() {
        //发送请求获取typeList
        this.typeList = [
            {
                type: 0,
                description: '设计'
            },
            {
                type: 1,
                description: '论文'
            }
        ]
        //发送请求获取sourceList
        this.sourceList = [
            {
                source: 0,
                description: '自命题'
            },
            {
                source: 1,
                description: '企业'
            },
            {
                source: 2,
                description: '教授科研'
            },
        ]

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