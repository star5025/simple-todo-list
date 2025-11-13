import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'

const service = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
})

service.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        if (token) {
            // 根据测试结果，后端期望的是token头部而不是Authorization
            config.headers.token = token
        }
        return config
    },
    (error) => {
        ElMessage.error('请求发送失败，请检查网络')
        return Promise.reject(error)
    }
)

service.interceptors.response.use(
    (response) => {
        // 第一部分：HTTP 200 响应（处理后端 JSON 中的业务 code）
        const res = response.data
        if (res.code === 1) {
            return res
        }
        else if (res.code === 401) {
            // 业务 code=401（HTTP 状态码 200，但后端提示 Token 过期）
            ElMessage.error(res.msg || '登录已过期，请重新登录')
            localStorage.removeItem('token')
            localStorage.removeItem('userId')
            localStorage.removeItem('userName')
            if (router) router.replace('/login')
            return Promise.reject(new Error(res.msg || '登录已过期，请重新登录'))
        }
        else {
            // 对于其他业务错误码，返回完整的响应数据，而不是reject
            return res
        }
    },
    // 第二部分：HTTP 非 200 响应（处理 HTTP 状态码 401/404/500 等）
    (error) => {
        let errMsg = '请求失败'
        if (error.message.includes('timeout')) {
            errMsg = '请求超时，请稍后重试'
        } else if (error.response) {
            // 关键修改：新增 HTTP 401 状态码处理
            switch (error.response.status) {
                case 401: // 处理 HTTP 401（后端直接返回 401 状态码）
                    errMsg = '登录已过期，请重新登录'
                    localStorage.removeItem('token') // 清除无效 Token
                    localStorage.removeItem('userId')
                    localStorage.removeItem('userName')
                    if (router) router.replace('/login') // 跳转登录页
                    break
                case 404:
                    errMsg = '请求的接口不存在'
                    break
                case 500:
                    errMsg = '服务器内部错误，请联系管理员'
                    break
                default:
                    errMsg = error.response.data?.msg || '请求失败'
            }
        } else {
            errMsg = '网络连接失败，请检查网络'
        }
        ElMessage.error(errMsg)
        return Promise.reject(error)
    }
)

export default {
    get(url, params = {}) {
        return service({ url, method: 'get', params })
    },
    post(url, data = {}) {
        return service({ url, method: 'post', data })
    },
    put(url, data = {}) {
        return service({ url, method: 'put', data })
    },
    delete(url, params = {}) {
        return service({ url, method: 'delete', params })
    },
    patch(url, data = {}) {
        return service({ url, method: 'patch', data })
    }
}
