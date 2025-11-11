import axios from 'axios'
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
    const res = response.data // 后端返回的完整JSON（含code、data、msg）
    
    if (res.code === 1) {
      return res 
    } 
    else if (res.code === 401) {
      ElMessage.error(res.msg || '登录已过期，请重新登录')
      localStorage.removeItem('token')
      // TODO 这个地方到时候转到登录界面的逻辑得优化
      if (router.currentRoute.value.path !== '/login') {
        router.push('/login')
      }
      return Promise.reject(res) 
    } 
    else {
      ElMessage.error(res.msg || '操作失败')
      return Promise.reject(res) // 抛出错误，组件可处理异常
    }
  },
  // 网络错误/HTTP状态码错误（如404、500，非后端返回的code）
  (error) => {
    let errMsg = '请求失败'
    if (error.message.includes('timeout')) {
      errMsg = '请求超时，请稍后重试'
    } else if (error.response) {
      // 处理HTTP状态码（如404接口不存在、500服务器错误）
      switch (error.response.status) {
        case 404:
          errMsg = '请求的接口不存在'
          break
        case 500:
          errMsg = '服务器内部错误，请联系管理员'
          break
        default:
          // 若后端在HTTP错误状态码中也返回了code/msg，优先显示
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
  }
}