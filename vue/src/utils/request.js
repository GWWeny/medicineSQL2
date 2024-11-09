import axios from 'axios'
import router from "@/router";

// 创建一个新的axios对象
const request = axios.create({
    baseURL: 'http://localhost:8080',   // 后端的接口地址  ip:port
    timeout: 30000
})

// request 拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    // 获取存储在localStorage中的用户信息
    let user = JSON.parse(localStorage.getItem("localStorageUser") || '{}');
    const token = user.token;

    // 如果存在token，加入到请求头
    if (token) {
        config.headers['token'] = token;  // 设置请求头中的token
    }

    return config;
}, error => {
    console.error('request error: ' + error) // for debug
    return Promise.reject(error);
});

// response 拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;

        // 如果服务器返回的是字符串，转换成JSON对象
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }

        // 如果返回的状态是 401，跳转到登录页
        if (res.code === 401) {
            router.push('/login');
        }

        return res;
    },
    error => {
        console.error('response error: ' + error); // for debug
        return Promise.reject(error);
    }
)

export default request;
