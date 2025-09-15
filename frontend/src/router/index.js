import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Users from '../views/Users.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: () => import('../views/Register.vue') },
  { path: '/users', component: Users },

  // 已有
  { path: '/points', component: () => import('../views/MonitoringPoints.vue') },
  { path: '/import', component: () => import('../views/DataImport.vue') },
  { path: '/thresholds', component: () => import('../views/Thresholds.vue') },
  { path: '/warnings', component: () => import('../views/Warnings.vue') },
  { path: '/history', component: () => import('../views/History.vue') },
  { path: '/gis', component: () => import('../views/GIS.vue') },

  // 新增：预测工作台（伪预测）
  { path: '/forecast', component: () => import('../views/ForecastStudio.vue') },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 添加路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  // 如果访问登录或注册页面
  if (to.path === '/login' || to.path === '/register') {
    // 如果已登录，跳转到主页
    if (token) {
      next('/users')
    } else {
      next()
    }
  } else {
    // 访问其他页面需要登录验证
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
