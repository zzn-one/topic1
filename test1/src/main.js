import Vue from 'vue'
import App from './App.vue'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from '@/router';
import axios from 'axios';
import Detail from '@/components/home/shared/Detail.vue';
import UserMsg from '@/components/home/shared/UserMsg.vue';
// import { Loading } from 'element-ui';

// axios配置
axios.defaults.baseURL = "http://localhost:8080"
// let loadingInstance = null
//axios请求拦截器
axios.interceptors.request.use(config => {

  //jwt
  const jwt = localStorage.getItem("jwt")
  if (jwt !== null) {
    //为请求头设置jwt
    
    config.headers.Authorization = jwt
  }
  //设置loading效果
  // loadingInstance = Loading.service({ fullscreen: true,text:"拼命加载中...",spinner:"el-icon-loading",background:"rgba(0,0,0,0)" })

  return config
})

//axios响应拦截器
axios.interceptors.response.use(response => {

  //关闭loading
  // loadingInstance.close()

  //如果jwt过期，则删除本地jwt
  if (response.data.code === 401) {
    localStorage.removeItem("jwt")
  }

  return response

})


Vue.prototype.$http = axios

//引入插件
Vue.use(ElementUI);

//全局注册组件
Vue.component("Detail", Detail);
Vue.component("UserMsg", UserMsg);

Vue.config.productionTip = false

const app = new Vue({
  render: h => h(App),
  router,
})



app.$mount('#app')
