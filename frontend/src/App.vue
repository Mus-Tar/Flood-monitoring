<template>
  <div class="min-h-screen">
    <!-- 只有登录后才显示导航栏 -->
    <header v-if="isLoggedIn" class="bg-gradient-to-r from-blue-600 via-blue-700 to-indigo-800 shadow-xl">
      <div class="mx-auto max-w-7xl px-4 py-3">
        <!-- 顶部品牌区域 -->
        <div class="flex items-center justify-between mb-3">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 bg-white/20 rounded-xl flex items-center justify-center backdrop-blur-sm">
              <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.002 4.002 0 003 15z"></path>
              </svg>
            </div>
            <div>
              <h1 class="text-xl font-bold text-white">洪水监测系统</h1>
              <p class="text-blue-200 text-sm">智能水文监测与预警平台</p>
            </div>
          </div>
          
          <!-- 用户信息和登出 -->
          <div class="flex items-center gap-4">
            <div class="flex items-center gap-3 px-4 py-2 bg-white/10 rounded-lg backdrop-blur-sm">
              <div class="w-8 h-8 bg-gradient-to-r from-blue-400 to-purple-500 rounded-full flex items-center justify-center">
                <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                </svg>
              </div>
              <span class="text-white font-medium">{{ username }}</span>
            </div>
            <button 
              @click="logout" 
              class="px-4 py-2 bg-red-500/80 hover:bg-red-600 text-white rounded-lg transition-all duration-200 backdrop-blur-sm hover:shadow-lg"
            >
              <svg class="w-4 h-4 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
              </svg>
              登出
            </button>
          </div>
        </div>

        <!-- 导航菜单 -->
        <nav class="flex items-center gap-2">
          <!-- 基础数据管理 -->
          <div class="flex items-center gap-1">
            <router-link to="/points" 
              class="flex items-center gap-2 px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm"
              :class="$route.path === '/points' ? 'bg-white/20 text-white shadow-lg' : ''">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
              </svg>
              监测点
            </router-link>

            <router-link to="/import" 
              class="flex items-center gap-2 px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm"
              :class="$route.path === '/import' ? 'bg-white/20 text-white shadow-lg' : ''">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
              </svg>
              数据导入
            </router-link>
          </div>

          <div class="w-px h-6 bg-blue-400/30 mx-2"></div>

          <!-- 配置管理 -->
          <div class="flex items-center gap-1">
            <router-link to="/thresholds" 
              class="flex items-center gap-2 px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm"
              :class="$route.path === '/thresholds' ? 'bg-white/20 text-white shadow-lg' : ''">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4"></path>
              </svg>
              阈值配置
            </router-link>

            <router-link to="/users" 
              class="flex items-center gap-2 px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm"
              :class="$route.path === '/users' ? 'bg-white/20 text-white shadow-lg' : ''">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
              </svg>
              用户管理
            </router-link>
          </div>

          <div class="w-px h-6 bg-blue-400/30 mx-2"></div>

          <!-- 监控预警 -->
          <div class="flex items-center gap-1">
            <router-link to="/warnings" 
              class="flex items-center gap-2 px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm"
              :class="$route.path === '/warnings' ? 'bg-white/20 text-white shadow-lg' : ''">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
              </svg>
              预警列表
            </router-link>

            <router-link to="/gis" 
              class="flex items-center gap-2 px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm"
              :class="$route.path === '/gis' ? 'bg-white/20 text-white shadow-lg' : ''">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7"></path>
              </svg>
              GIS地图
            </router-link>
          </div>

          <div class="w-px h-6 bg-blue-400/30 mx-2"></div>

          <!-- 分析预测 -->
          <div class="flex items-center gap-1">
            <router-link to="/history" 
              class="flex items-center gap-2 px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm"
              :class="$route.path === '/history' ? 'bg-white/20 text-white shadow-lg' : ''">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
              </svg>
              历史趋势
            </router-link>

            <router-link to="/forecast" 
              class="flex items-center gap-2 px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm"
              :class="$route.path === '/forecast' ? 'bg-white/20 text-white shadow-lg' : ''">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path>
              </svg>
              预测工作台
            </router-link>
          </div>
        </nav>
      </div>
    </header>
    
    <!-- 主内容区 -->
    <main :class="isLoggedIn ? 'mx-auto max-w-7xl p-4' : ''">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const isLoggedIn = ref(false)
const username = ref('')

// 检查登录状态
const checkLoginStatus = () => {
  const token = localStorage.getItem('token')
  const storedUsername = localStorage.getItem('username')
  
  isLoggedIn.value = !!token
  username.value = storedUsername || ''
}

// 登出功能
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  isLoggedIn.value = false
  username.value = ''
  router.push('/login')
}

// 监听路由变化，更新登录状态
watch(route, () => {
  checkLoginStatus()
})

onMounted(() => {
  checkLoginStatus()
})
</script>
