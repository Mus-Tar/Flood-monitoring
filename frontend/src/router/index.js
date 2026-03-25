import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Users from '../views/Users.vue'
import { getAccessToken, getUserInfo } from '../utils/auth'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: () => import('../views/Register.vue') },
  { path: '/users', component: Users },
  { path: '/points', component: () => import('../views/MonitoringPoints.vue') },
  { path: '/import', component: () => import('../views/DataImport.vue') },
  { path: '/thresholds', component: () => import('../views/Thresholds.vue') },
  { path: '/warnings', component: () => import('../views/Warnings.vue') },
  { path: '/history', component: () => import('../views/History.vue') },
  { path: '/gis', component: () => import('../views/GIS.vue') },
  { path: '/forecast', component: () => import('../views/ForecastStudio.vue') }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = getAccessToken()
  const userInfo = getUserInfo()
  const role = userInfo?.role || 'USER'
  const adminPages = ['/points', '/import', '/thresholds', '/users']

  if (to.path === '/login' || to.path === '/register') {
    if (token) {
      next(role === 'ADMIN' ? '/users' : '/warnings')
      return
    }
    next()
    return
  }

  if (!token) {
    next('/login')
    return
  }

  if (role !== 'ADMIN' && adminPages.includes(to.path)) {
    next('/warnings')
    return
  }

  next()
})

export default router
