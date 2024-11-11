import Vue from "vue";//导入Vue.js核心库
import App from './App.vue'//导入App，在Vue渲染，承载应用的其他部件
import router from "@/router";//导入Vue路由对象，负责页面间的切换
import ElementUI from 'element-ui';//导入Element UI库，有很多组件
import 'element-ui/lib/theme-chalk/index.css';//导入Element UI的样式


import request from '@/utils/request'//导入自定义请求工具，用来处理前后端交互

Vue.config.productionTip = false//关闭 Vue 的生产提示
Vue.use(ElementUI, { size: 'small' });//所有应用使用 Element UI 插件，默认small
Vue.prototype.$request = request;//将 request 工具方法添加到 Vue 的原型中, 这样所有组件都可以使用 $request 调用
new Vue({
    router,//将路由对象挂载到Vue实例上
    render: h => h(App)//将App渲染到页面上
}).$mount('#app')


