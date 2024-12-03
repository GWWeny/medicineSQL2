import Vue from 'vue'//导入 Vue 构造函数，这是 Vue.js 的核心库
import VueRouter from 'vue-router'//用于处理应用中的路由（即不同 URL 映射到不同的组件）

const originPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originPush.call(this, location).catch(err => err)
}


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
        redirect: '/home',
        name: 'Manager',
        component: () => import('@/components/Manager.vue'),
        children: [
            {path: 'home', name: 'Home', meta: {title: '首页'}, component: () => import('@/components/action/Home.vue')},
            {path:'medicine', name: 'Medicine', meta: {title: '药品管理'}, component: () => import('@/components/action/Medicine.vue')},
            {path: '403', name: '403', meta: {title: '无权限'}, component: () => import('@/components/action/403.vue')},
            {path: 'password', name: 'Password', meta: {title: '修改密码'}, component: () => import('@/components/action/Password.vue')},
            {path: '/user', name: 'User', meta: {title: '用户管理'}, component: () => import('@/components/action/User.vue')},
            {path: 'agency',name: 'Agency', meta: {title: '经办人管理'}, component: () => import('@/components/action/Agency.vue')},
            {path: 'client',name:'Client', meta: {title: '客户管理'},component: () => import('@/components/action/Client.vue')}
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
    const whiteList = ['/login', '/register', '/403', '/404']; // 白名单，不需要权限的页面

    // 获取存储的用户信息
    const userStr = localStorage.getItem('localStorageUser');
    const localStorageUser = userStr ? JSON.parse(userStr) : null;

    // 如果 localStorageUser 为 null，直接跳转到登录页面
    if (!localStorageUser) {
        if (!whiteList.includes(to.path)) {
            return next('/login'); // 没有用户信息，跳转到登录页
        }
    }

    // 获取 token 和过期时间，若没有则跳转到登录
    const token = localStorageUser?.data?.token; // 获取 token
    const expireTime = localStorageUser?.data?.expireTime; // 获取过期时间
    const role = localStorageUser ? localStorageUser.data.role : null;//获取身份信息

    // 如果访问的是需要身份验证的页面且没有 token
    if (!token && !whiteList.includes(to.path)) {
        return next('/login'); // 如果没有 token，强制跳转到登录页面
    }

    // 如果访问的是登录页面，且已经有 token（用户已经登录），跳转到首页
    if (to.path === '/login' && token) {
        return next('/home'); // 如果已经登录，直接跳转到首页
    }

    // 检查 token 是否过期
    const currentTime = new Date().getTime();
    if (token && expireTime && currentTime > expireTime) {
        // 如果 token 过期，清除 localStorage 中的用户信息并跳转到登录页
        localStorage.removeItem('localStorageUser');
        return next('/login');
    }

    if(to.path === '/client' && role !== 'admin'){
        return next('/403');
    }

    // 如果没有问题，正常访问
    next();
});


export default router
