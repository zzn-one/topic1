<template>
    <el-dialog title="修改个人信息" :visible.sync="dialogVisible" :show-close="false" :close-on-press-escape="false"
        :close-on-click-modal="false">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <!-- <el-form-item label="头像" prop="pass">
                <el-input  type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
            </el-form-item> -->
            <el-form-item label="账号" prop="id">
                <el-input type="text" v-model="ruleForm.id" autocomplete="off" disabled></el-input>
            </el-form-item>
            <el-form-item label="用户名" prop="name">
                <el-input type="text" v-model="ruleForm.name" autocomplete="off" disabled></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input type="text" v-model="ruleForm.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input type="text" v-model.number="ruleForm.email"></el-input>
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
export default {
    name: "UpdateUserMsg",
    props: ["visible"],
    emits: ['close'],
    data() {
        var validatePhone = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入手机号'));
            } else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        var validateEmail = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入邮箱'));
            } else {
                callback();
            }
        };
        return {
            ruleForm: {
                id:'',
                name:'',
                phone: '',
                email: '',
            },
            rules: {
                phone: [
                    { validator: validatePhone, trigger: 'blur' }
                ],
                email: [
                    { validator: validateEmail, trigger: 'blur' }
                ]
            },
            dialogVisible: false


        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    alert('submit!');
                    //验证成功，发送aixos请求
                } else {
                    console.log('error submit!!');
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
        }
    },
    watch: {
        visible(newValue) {
            this.dialogVisible = newValue
        }
    },
    created(){
        //获取用户信息
        this.ruleForm.id=1
        this.ruleForm.name='搜索树'
        this.ruleForm.phone='12232323'
        this.ruleForm.email='222@www.cccom'
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