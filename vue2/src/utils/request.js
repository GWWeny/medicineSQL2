import axios from 'axios'

// 创建可一个新的axios对象
const request = axios.create({
    baseURL: 'http://localhost:8080',   // 后端的接口地址  ip:port
    timeout: 30000,
})


// request 拦截器
// xianggu
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
        const userStr = localStorage.getItem('localStorageUser');
        const localStorageUser = userStr ? JSON.parse(userStr) : null;
        if (localStorageUser && localStorageUser.data.token) {
            config.headers['token'] = localStorageUser.data.token;
        }
        return config;
    },
    (error)=>{
    return Promise.reject(error);
    }

);


export default request

