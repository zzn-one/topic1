<template>
    <el-form :model="formData" size="medium" status-icon :rules="rules" ref="loginForm" label-width="50px"
        class="demo-ruleForm" label-position="left">
        <el-form-item></el-form-item>
        <el-form-item :label="accountName" prop="account">
            <el-input :placeholder="'请输入'+$props.accountName" type="text" v-model="formData.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input placeholder="请输入密码" type="password" v-model="formData.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
            <el-row class="btn">
                <el-button plain class="btn-login" type="primary" @click="submitForm('loginForm')">登录</el-button>
                <el-button plain class="btn-reset" type="danger" @click="resetForm('loginForm')">重置</el-button>
            </el-row>
        </el-form-item>
    </el-form>
</template>
<script>

export default {
    name: "LoginForm",
    props: ["accountName", "userType"],

    data() {
        var validatePassword = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                callback();
            }
        };
        var validateAccount = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入' + this.$props.accountName));
            } else {
                callback();
            }
        };

        return {
            formData: {
                password: '',
                account: ''

            },
            rules: {
                password: [
                    { validator: validatePassword, trigger: 'blur' }
                ],
                account: [{
                    validator: validateAccount, trigger: 'blur'
                }]

            }
        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //前端校验通过，根据type选择相应接口 发送ajax请求
                    alert('submit!'+this.userType);

                    //如果身份验证成功，跳转到/home
                    this.$router.push({
                        name:'home',
                    })
                } else {
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        }
    }
}
</script>
<style scoped lang="less">
/deep/.el-form-item__label {
    font-size: 18px;
}

/deep/.el-form-item__error {
    font-size: 16px;
}

.btn-login,
.btn-reset {
    font-size: 18px;
}
.btn{
    margin-top: 20px;
    margin-right: 20px;
}

.btn-login {
    margin-right: 30px;
}


</style>