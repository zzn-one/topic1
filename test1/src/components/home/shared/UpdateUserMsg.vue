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
                <el-input type="text" v-model="ruleForm.email"></el-input>
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
    name: "UpdateUserMsg",
    props: ["visible"],
    emits: ['close'],
    data() {

        return {
            ruleForm: {
                id: '',
                name: '',
                phone: '',
                email: '',
            },
            rules: {
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur'},
                    { min: 11, max: 11, message: '长度为11位数字', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur'},
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
                    this.updateMsg()
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
        async getMsg() {
            //获取用户信息
            let id = jwtPrase().id
            let name = jwtPrase().name

            let userType = jwtPrase().userType
            let url;

            if (userType == 0) {
                //毕业生
                url="/userStudent/"
            } else if (userType == 1) {
                //导师
                url="/userTutor/"

            } else if (userType == 2) {
                //管理员
                url="/userAdmin/"
            }

            const resp = await this.$http.get(url + id)

            if (resp.data.code === 200) {
                let user = resp.data.data
                this.ruleForm.id = id
                this.ruleForm.name = name
                this.ruleForm.phone = user.phone
                this.ruleForm.email = user.email
            }
        },
        async updateMsg(){
            let userType = jwtPrase().userType
            let url;

            if (userType == 0) {
                //毕业生
                url="/userStudent"
            } else if (userType == 1) {
                //导师
                url="/userTutor"

            } else if (userType == 2) {
                //管理员
                url="/userAdmin"
            }
            url+="/updateMsg"

            const resp = await this.$http({
                method:'put',
                url: url,
                data:this.ruleForm
            })
            if (resp.data.code === 200) {
                let result=resp.data
                this.$message({
                    message:result.msg,
                    type:'success'
                })
            }
        }
    },
    watch: {
        visible(newValue) {
            this.dialogVisible = newValue
            this.getMsg()
        }
    },
    created() {
        this.getMsg()
    },
    
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