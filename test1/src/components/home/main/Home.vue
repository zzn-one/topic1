<template>
  <el-container>
    <el-main class="main_container bgColor">
      <el-container>
        <el-aside :width="asideWidth + 'px'">
          <div class="aside-top">
            毕业设计选题管理系统
          </div>
          <el-menu :collapse="menuClose">
            <Menu :type="userType"></Menu>
          </el-menu>
        </el-aside>
        <el-main>
          <el-container>
            <el-header>
              <div class="header-div">
                <span class="header-switch" :title="switchTitle" :class="menuSwitchClass" @click="showMenu"></span>
                <span class="header-title">
                  高校智能毕业设计选题管理系统
                </span>
                <span class="header-img" @click="showUserMsg">
                  <!-- 用户头像 -->
                  <el-avatar :src="imgUrl" title="个人中心"></el-avatar>
                </span>


              </div>
            </el-header>

            <el-main class="main">
              <transition name="el-zoom-in-center">
                <RouterView></RouterView>
              </transition>

            </el-main>

            <el-footer>v1.0</el-footer>
          </el-container>
        </el-main>
      </el-container>
    </el-main>



  </el-container>
</template>

<script>
import Menu from '@/components/home/main/Menu.vue'
import router from '@/router'
import { RouterView } from 'vue-router'
import jwtPrase from '@/assets/js/jwtPrase';


export default {
  name: "Home",
  components: {
    Menu,
    router,
    RouterView
  },
  data() {
    return {
      menuClose: false,
      userType: '',
      asideWidth: 200,
      menuSwitchClass: "el-icon-s-fold",
      switchTitle: "隐藏操作菜单",
      imgUrl: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
    }
  },
  methods: {
    showMenu() {
      this.menuClose = !this.menuClose
      if (this.menuClose) {//关闭菜单
        this.asideWidth = 0
        this.menuSwitchClass = "el-icon-s-unfold"
        this.switchTitle = "显示操作菜单"
      } else {//打开菜单
        this.asideWidth = 200
        this.menuSwitchClass = "el-icon-s-fold"
        this.switchTitle = "隐藏操作菜单"
      }
    },
    showUserMsg() {//跳转到用户 个人中心 组件
      this.$router.push({
        name: 'userMsg'
      })
    },
    //获取毕业年份
    async getYear() {
      const resp = await this.$http.get("/dict/year")
      if (resp.data.code === 200) {
        localStorage.setItem("year", resp.data.data.year)
      }
    }

  },
  created() {
    //根据jwt获取用户类型

    let payload = jwtPrase()

    this.userType = payload.userType

    //获取毕业年份
    this.getYear()
  }


}
</script>

<style scoped lang="less">
.el-footer {
  background-color: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  color: #333;
  font-size: 18px;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(10px);
  color: #333;
  min-height: 800px;
  height: 969px;
  text-align: center;
  line-height: 200px;
}

/deep/.el-menu {
  background-color: rgba(225,243,255, 0.8);
  backdrop-filter: blur(10px);
}

.aside-top {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-weight: bolder;
}

/deep/.el-main {
  padding: 0;
}

body {
  margin: 0;

}

.main_container {
  color: #333;
  text-align: center;
  line-height: 160px;
  /*background-image: url("@/assets/bg2.jpg");*/
  background-repeat: round;
}


.bgColor {

  background: #aaddff;
  /* fallback for old browsers */
  background: -webkit-linear-gradient(to top, #e6f7fd, #bde4ff, #d5f4ff);
  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to top, #e6f7fd, #bde4ff, #d5f4ff);
  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

}

.el-header {
  background-color: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  color: #333;
  text-align: center;
  line-height: 60px;
}

.header-div {
  height: auto;
  line-height: 60px;
  display: flex;
}

.header-switch {
  flex: 1;
  height: 60px;
  line-height: 60px;
  font-size: 30px;
  margin-left: -20px;
  float: left;
  text-align: left;
  cursor: pointer;

}

.header-title {
  flex: 4;
  min-width: 400px;
  text-align: center;
  font-size: 20px;
  float: left;

}

.header-img {
  flex: 1;
  height: 60px;
  line-height: 60px;
  float: right;
  margin-top: 10px;
  text-align: right;
  cursor: pointer;
}

.main {
  height: 820px;
}
</style>
