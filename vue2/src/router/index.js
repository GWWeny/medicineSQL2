import Vue from 'vue'//导入 Vue 构造函数，这是 Vue.js 的核心库
import VueRouter from 'vue-router'//用于处理应用中的路由（即不同 URL 映射到不同的组件）

//使用 Vue.use() 方法安装 Vue Router 插件。这使得 Vue 实例可以使用路由功能，
// 包括页面跳转、路由嵌套等。VueRouter 是通过这个方法全局安装的
Vue.use(VueRouter)

const routes = [
    {
        path: '/login',//路径对应渲染对应组件
        name: 'LoginForm3', //路由名称，帮助导航
        component: () => import('@/components/LoginForm3.vue')//该路径对应的组件。这里使用了懒加载方式
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/components/Register.vue')
    },
    {
        path: '/',
        name: 'Manager',
        component: () => import('@/components/Manager.vue'),
        children: [
            {path: 'home', name: 'Home', meta: {title: '首页'}, component: () => import('@/components/action/Home.vue')},
            {path: '403', name: 'Auth', meta: {title: '无权限'}, component: () => import('@/components/action/403.vue')},
            {path: 'password', name: 'password', meta: {title: '修改密码'}, component: () => import('@/components/action/Password.vue')},

        ]
    },
    {path: '*', name: '404', meta: {title: '404'}, component: () => import('@/components/action/404.vue')},
]

//创建 VueRouter 实例，并传入一个包含所有路由规则的 routes 数组。
// 这个 router 实例将用于管理整个应用的路由。
const router=new VueRouter({
    mode: 'history', // 使用 history 模式，而不是默认的 hash 模式
    base: process.env.BASE_URL, // 设置基础路径，用于部署在子路径上
    routes,
})

router.beforeEach((to, from, next) => {
    let adminPaths = ['/user'];
    let localStorageUser = JSON.parse(localStorage.getItem('user') || '{}');

    if (localStorageUser.role !== 'admin' && adminPaths.includes(to.path)) {
        next('/403'); // 导航到 403 页面
    } else {
        next(); // 允许导航
    }
});


export default router
