const ACCESS_TOKEN_KEY = 'accessToken'
const REFRESH_TOKEN_KEY = 'refreshToken'
const USER_INFO_KEY = 'userInfo'

export const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_KEY) || ''

export const getRefreshToken = () => localStorage.getItem(REFRESH_TOKEN_KEY) || ''

export const getUserInfo = () => {
  const raw = localStorage.getItem(USER_INFO_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch {
    return null
  }
}

export const setAuthData = (payload = {}) => {
  const accessToken = payload.accessToken || ''
  const refreshToken = payload.refreshToken || ''
  const userInfo = payload.userInfo || null

  localStorage.setItem(ACCESS_TOKEN_KEY, accessToken)
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken)
  localStorage.setItem('token', accessToken)

  if (userInfo) {
    localStorage.setItem(USER_INFO_KEY, JSON.stringify(userInfo))
    localStorage.setItem('username', userInfo.username || '')
    localStorage.setItem('userRole', userInfo.role || 'USER')
  }
}

export const clearAuthData = () => {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
  localStorage.removeItem(REFRESH_TOKEN_KEY)
  localStorage.removeItem(USER_INFO_KEY)
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('userRole')
}

export const isAdminUser = () => {
  const userInfo = getUserInfo()
  return userInfo?.role === 'ADMIN'
}
