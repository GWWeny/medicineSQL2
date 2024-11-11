import axios from 'axios'
import router from "@/router";

// 创建可一个新的axios对象
const request = axios.create({
    baseURL: 'http://localhost:8080',   // 后端的接口地址  ip:port
    timeout: 30000
})


// request 拦截器
// xianggu
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    // let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    let user = JSON.parse(localStorage.getItem("localStorageUser") || '{}')
    console.log(user.token)
    config.headers['token'] = user.token  // 设置请求头
    return config
}, error => {
    console.error('request error: ' + error) // for debug
    return Promise.reject(error)
});


export default request

