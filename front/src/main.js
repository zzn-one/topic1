import Vue from 'vue'
import App from './App.vue'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from '@/router';
import axios from 'axios';
import Detail from '@/components/home/shared/Detail.vue';
import UserMsg from '@/components/home/shared/UserMsg.vue';

axios.defaults.baseURL="http://1.12.54.183:8888"

Vue.prototype.$http=axios

//引入插件
Vue.use(ElementUI);

//全局注册组件
Vue.component("Detail",Detail);
Vue.component("UserMsg",UserMsg);

Vue.config.productionTip = false

const app = new Vue({
  render: h => h(App),
  router,
})



app.$mount('#app')
