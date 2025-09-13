import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Users from '../views/Users.vue'

const routes = [
  { path: '/', redirect: '/users' },
  { path: '/login', component: Login },
  { path: '/users', component: Users },

  // 已有
  { path: '/points', component: () => import('../views/MonitoringPoints.vue') },
  { path: '/import', component: () => import('../views/DataImport.vue') },
  { path: '/thresholds', component: () => import('../views/Thresholds.vue') },
  { path: '/warnings', component: () => import('../views/Warnings.vue') },

  // 新增：历史趋势
  { path: '/history', component: () => import('../views/History.vue') },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
