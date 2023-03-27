<template>
    <div class="main bgImg bgColor">
        <div class="loginForm ">
            <el-tabs type="border-card" class="tabs ">
                <el-tab-pane label="管理员">
                    <LoginForm account-name="工号" userType="2"></LoginForm>
                </el-tab-pane>

                <el-tab-pane label="指导老师">
                    <LoginForm account-name="工号" userType="1"></LoginForm>
                </el-tab-pane>

                <el-tab-pane label="毕业生">
                    <LoginForm account-name="学号" userType="0"></LoginForm>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
import LoginForm from './LoginForm.vue';
export default {

    name: "Login",
    components: {
        LoginForm
    },
}
</script>
<style scoped lang="less">
/deep/.el-tabs--border-card>.el-tabs__header .el-tabs__item.is-active {
    color: rgb(242, 185, 169);
    background-color: rgb(255, 248, 237);
    border-right-color: #DCDFE6;
    border-left-color: #DCDFE6;
}

.main {
    display: flex;
   
    height: 950px;
    width: 100%;

}

.bgColor {

    background: #86c8f3;
    /* fallback for old browsers */
    background: -webkit-linear-gradient(to top, #FFFFFF, #6DD5FA, #86c8f3);
    /* Chrome 10-25, Safari 5.1-6 */
    background: linear-gradient(to top, #FFFFFF, #6DD5FA, #86c8f3);
    /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

}

.bgImg {
    /* background-image: url(@/assets/bg2.jpg);*/
    background-repeat: round;
}

.loginForm {
    width: 363px;
    margin: 0 auto;
    display: flex;
    justify-content: center;
    align-items: center;
}

/deep/.el-tabs__item {
    font-size: 24px;
    flex: 1;
}

.tabs {
    text-align: center;
    min-width: 363px;
    width: 363px;
    min-height: 300px;
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
}

/deep/.el-tabs--border-card>.el-tabs__header {
    background-color: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
}</style>
