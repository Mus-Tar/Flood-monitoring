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
  const userRole = localStorage.getItem('userRole') || 'USER'
  
  // 管理员专属页面
  const adminPages = ['/points', '/import', '/thresholds', '/users']
  
  // 如果访问登录或注册页面
  if (to.path === '/login' || to.path === '/register') {
    // 如果已登录，跳转到适当的主页
    if (token) {
      // 管理员跳转到用户管理，普通用户跳转到预警列表
      next(userRole === 'ADMIN' ? '/users' : '/warnings')
    } else {
      next()
    }
  } else {
    // 访问其他页面需要登录验证
    if (token) {
      // 检查权限：普通用户不能访问管理员页面
      if (userRole !== 'ADMIN' && adminPages.includes(to.path)) {
        // 普通用户试图访问管理员页面，重定向到预警列表
        next('/warnings')
      } else {
        next()
      }
    } else {
      next('/login')
    }
  }
})

export default router
