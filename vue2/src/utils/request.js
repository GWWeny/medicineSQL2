import axios from 'axios'
import router from "@/router";

// 创建可一个新的axios对象
const request = axios.create({
    baseURL: 'http://localhost:8080',   // 后端的接口地址  ip:port
    timeout: 30000
})


export default request

