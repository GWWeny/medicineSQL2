import Vue from 'vue'
import VueRouter from 'vue-router'

//防止用户重复点击而导致的导航重复问题
const originPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originPush.call(this, location).catch(err => err)
}
//安装Vue Router插件，允许vue应用使用路由功能
Vue.use(VueRouter)
//路由定义
const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('../components/LoginForm3.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../components/Register.vue')
    },
    {
        path: '/',
        name: 'Manager',
        component: () => import('../components/Manager.vue'),
        children: [
            { path: 'home', name: 'SystemHomePage', meta: { title: '首页' }, component: () => import('../components/action/Home.vue') },//懒加载，优化页面加载速度
            { path: 'user', name: 'user', meta: { title: '个人信息' }, component: () => import('../components/action/User.vue') },
            { path: '403', name: '403', meta: { title: '无权限' }, component: () => import('../components/action/403.vue') },
            { path: 'password', name: 'ChangePassword', meta: { title: '修改密码' }, component: () => import('../components/action/Password.vue') },
            { path: 'medicine', name: 'Medicine', meta: { title: '药品管理' }, component: () => import('../components/action/Medicine.vue') },
            { path: 'agency', name: 'Agency', meta: { title: '代理商管理' }, component: () => import('../components/action/Agency.vue') },
            { path: 'client', name: 'Client', meta: { title: '客户管理' }, component: () => import('../components/action/Client.vue') }
        ]
    },
    { path: '*', name: '404', meta: { title: '404' }, component: () => import('../components/action/404.vue') },
]

//注意路径重定向问题，不然会404
const router = new VueRouter({
    mode: 'history',//路由模式，利用浏览器的History API，让URL显示为常见的路径格式，默认的hash模式会让路径中包含#，/#/user
    base: process.env.BASE_URL,//环境变量？
    routes//路由配置数组，用于将路径和组件映射
})

// 路由守卫
router.beforeEach((to, from, next) => {

    //延时
    setTimeout(() => {
    let adminPaths = ['/medicine', '/agency', '/client']//定义只允许管理员访问的路由
    //cong localStorage获取当前用户信息，将其解析为对象形式，user数据通常是在用户登录后保存的，包含用户的角色信息
    let localStorageUser = JSON.parse(localStorage.getItem('localStorageUser') || '{}')
    //如果用户未登录，且试图访问其他界面，将会重定向到登陆界面
    const isLoggedIn = localStorage.getItem('localStorageUser');
    console.log("路由守卫判断是否登录:", isLoggedIn);
    if(!isLoggedIn && to.path!=='/login'){
        next('/login')
        return;
    }else{
        next();
    }

        if (localStorageUser.role !== "admin" && adminPaths.includes(to.path)) {
            next('/403')//导航到403页面，表示禁止访问，但疑似没有效果,经验证，有效果了
        } else {
            next()//允许导航到目标路由
        }


    }, 0);
})
export default router
