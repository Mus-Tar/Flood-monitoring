import axios from 'axios'
import { clearAuthData, getAccessToken, getRefreshToken, setAuthData } from './auth'

const instance = axios.create({
  baseURL: 'http://localhost:8080'
})

let isRefreshing = false
let pendingQueue = []

const flushQueue = (error, token = '') => {
  pendingQueue.forEach(({ resolve, reject }) => {
    if (error) {
      reject(error)
    } else {
      resolve(token)
    }
  })
  pendingQueue = []
}

instance.interceptors.request.use(config => {
  const token = getAccessToken()
  if (token) {
    config.headers = config.headers || {}
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

instance.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config || {}
    const status = error.response?.status

    if (status !== 401 || originalRequest._retry || originalRequest.url?.includes('/api/auth/refresh')) {
      if (status === 401) {
        clearAuthData()
      }
      return Promise.reject(error)
    }

    const refreshToken = getRefreshToken()
    if (!refreshToken) {
      clearAuthData()
      window.location.hash = '#/login'
      return Promise.reject(error)
    }

    if (isRefreshing) {
      return new Promise((resolve, reject) => {
        pendingQueue.push({
          resolve: token => {
            originalRequest.headers = originalRequest.headers || {}
            originalRequest.headers.Authorization = `Bearer ${token}`
            resolve(instance(originalRequest))
          },
          reject
        })
      })
    }

    originalRequest._retry = true
    isRefreshing = true

    try {
      const response = await axios.post('http://localhost:8080/api/auth/refresh', { refreshToken })
      const payload = response.data?.data
      if (!payload?.accessToken) {
        throw new Error('刷新失败')
      }

      setAuthData(payload)
      flushQueue(null, payload.accessToken)

      originalRequest.headers = originalRequest.headers || {}
      originalRequest.headers.Authorization = `Bearer ${payload.accessToken}`
      return instance(originalRequest)
    } catch (refreshError) {
      flushQueue(refreshError)
      clearAuthData()
      window.location.hash = '#/login'
      return Promise.reject(refreshError)
    } finally {
      isRefreshing = false
    }
  }
)

export default instance
