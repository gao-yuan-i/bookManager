import axios from 'axios'
import { getToken, clearAuth } from '@/utils/auth'
import { useRouter } from 'vue-router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器：自动带 token
request.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器：统一处理错误
request.interceptors.response.use(
  response => {
    const res = response.data
    // 后端返回 code=200 才是成功
    if (res.code === 200) {
      return res
    }
    // code!=200 是业务异常
    alert(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        clearAuth()
        // 跳转登录页
        window.location.href = '/login'
        return Promise.reject(new Error('登录已过期，请重新登录'))
      }
      if (status === 403) {
        alert('没有权限访问')
        return Promise.reject(new Error('没有权限访问'))
      }
    }
    alert('网络异常，请稍后重试')
    return Promise.reject(error)
  }
)

export default request