import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Users from '../views/Users.vue'

const routes = [
  { path: '/', redirect: '/users' },
  { path: '/login', component: Login },
  { path: '/users', component: Users },

  // 新增：
  { path: '/points', component: () => import('../views/MonitoringPoints.vue') },
  { path: '/import', component: () => import('../views/DataImport.vue') },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
