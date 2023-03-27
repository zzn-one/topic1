<template>
    <el-dialog title="修改密码" :visible.sync="dialogVisible" :show-close="false" :close-on-press-escape="false"
        :close-on-click-modal="false">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="账号" prop="id">
                <el-input type="text" v-model="ruleForm.id" autocomplete="off" disabled></el-input>
            </el-form-item>
            <el-form-item label="用户名" prop="name">
                <el-input type="text" v-model="ruleForm.name" autocomplete="off" disabled></el-input>
            </el-form-item>
            <el-form-item label="旧密码" prop="pass">
                <el-input placeholder="请输入旧密码" type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
                <el-input placeholder="请确认旧密码" type="password" v-model="ruleForm.checkPass"
                    autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPass">
                <el-input type="password" placeholder="请输入新密码" v-model.number="ruleForm.newPass"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button class="btn" plain type="primary" @click="submitForm('ruleForm')">提交</el-button>
                <el-button class="btn" plain type="warning" @click="resetForm('ruleForm')">重置</el-button>
                <el-button class="btn" plain type="danger" @click="closeDialog">取 消</el-button>
            </el-form-item>

        </el-form>

    </el-dialog>
</template>
<script>
import jwtPrase from '@/assets/js/jwtPrase';

export default {
    name: "UpdatePassword",
    props: ["visible"],
    emits: ['close'],
    data() {
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.ruleForm.pass) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        var validateNewPass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入新密码'));
            } else {
                callback();
            }
        };
        return {
            ruleForm: {
                id: '',
                name: '',
                pass: '',
                checkPass: '',
                newPass: ''
            },
            rules: {
                pass: [
                    { validator: validatePass, trigger: 'blur' }
                ],
                checkPass: [
                    { validator: validatePass2, trigger: 'blur' }
                ],
                newPass: [
                    { validator: validateNewPass, trigger: 'blur' }
                ]
            },
            dialogVisible: false


        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //验证成功，发送aixos请求
                    this.updatePassword()
                } else {
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        closeDialog() {
            this.dialogVisible = false
            this.$emit('close', false)
        },
        getUserMsg() {
            //获取用户信息
            this.ruleForm.id = jwtPrase().id
            this.ruleForm.name = jwtPrase().name
        },
        async updatePassword() {
            
            const userType = jwtPrase().userType
            let url;

            if (userType == 0) {
                url="/userStudent"

                //毕业生
            } else if (userType == 1) {
                url="/userTutor"

                //导师
            } else if (userType == 2) {
                //管理员
                url="/userAdmin"
            }
            url+="/updatePassword"

            const resp = await this.$http({
                url: url,
                data: this.ruleForm,
                method: "put"
            })


            if (resp.data.code == 200) {
                this.$message({
                    message: resp.data.msg,
                    type: "success"
                })
                this.closeDialog()
            } else if (resp.data.code == 403) {
                this.$message({
                    message: resp.data.msg,
                    type: "error"
                })
            }
        }
    },
    watch: {
        visible(newValue) {
            this.dialogVisible = newValue
        }
    },
    created() {
        //获取用户信息
        this.getUserMsg()
    }
}
</script>
<style scoped lang="less">
/deep/.el-form-item__label {
    font-size: 18px;
    color: black;
}

/deep/.el-form-item__error {
    font-size: 14px;
}

/deep/.el-dialog__header {
    line-height: 24px;
}

/deep/.el-dialog {
    background-color: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(10px);
    width: 480px;
    min-width: 480px;

}

.btn {
    font-size: 18px;
}
</style>