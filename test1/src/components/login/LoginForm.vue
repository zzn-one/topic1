<template>
    <el-form :model="formData" size="medium" status-icon :rules="rules" ref="loginForm" label-width="50px"
        class="demo-ruleForm" label-position="left">
        <el-form-item></el-form-item>
        <el-form-item :label="accountName" prop="account">
            <el-input :placeholder="'请输入' + $props.accountName" type="text" v-model="formData.account"
                autocomplete="off"></el-input>
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
                    this.login()

                } else {
                    return false;
                }
            });
        },
        async login() {
            let resp;

            if (this.userType == '0') {
                //毕业生
                resp = await this.$http.get("/userStudent/login/" + this.formData.account + "/" + this.formData.password)
            } else if (this.userType == '1') {
                //导师
                resp = await this.$http.get("/userTutor/login/" + this.formData.account + "/" + this.formData.password)
            } else if (this.userType == '2') {
                //管理员
                resp = await this.$http.get("/userAdmin/login/" + this.formData.account + "/" + this.formData.password)
            }

            if (resp.data.code === 200) {
                //验证通过
                //1.全局存储jwt
                const jwt = resp.data.data

                localStorage.setItem("jwt", jwt)

                //2.跳转到首页
                this.$message({
                    message: "欢迎使用高校智能毕业设计后台管理系统！",
                    type: 'success'
                })
                this.toHome()

            } else if (resp.data.code === 401) {
                //身份验证失败
                this.$alert("账号或密码错误！", {
                    type: "warning"
                })
            } else {
                //其他异常
                this.$alert("系统出错了，请稍后再尝试登录。", {
                    type: "warning"
                })
            }
        },
        toHome() {
            //如果身份验证成功，跳转到timeLine
            this.$router.push({
                name: 'timeLine',
            })
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

.btn {
    margin-top: 20px;
    margin-right: 20px;
}

.btn-login {
    margin-right: 30px;
}
</style>