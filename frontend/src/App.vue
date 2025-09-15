<template>
  <div class="min-h-screen">
    <!-- 只有登录后才显示导航栏 -->
    <header v-if="isLoggedIn" class="bg-white shadow">
      <div class="mx-auto max-w-7xl px-4 py-4 flex items-center justify-between">
        <h1 class="text-lg font-semibold">洪水监测系统</h1>
        <div class="flex items-center space-x-6">
          <nav class="space-x-4">
            <router-link to="/users" class="text-blue-600 hover:underline">用户管理</router-link>
            <router-link to="/points" class="text-blue-600 hover:underline">监测点</router-link>
            <router-link to="/import" class="text-blue-600 hover:underline">数据导入</router-link>
            <router-link to="/thresholds" class="text-blue-600 hover:underline">阈值配置</router-link>
            <router-link to="/warnings" class="text-blue-600 hover:underline">预警列表</router-link>
            <router-link to="/history" class="text-blue-600 hover:underline">历史趋势</router-link>
            <router-link to="/gis" class="text-blue-600 hover:underline">GIS 地图</router-link>
            <router-link to="/forecast" class="text-blue-600 hover:underline">预测工作台</router-link>
          </nav>
          
          <!-- 用户信息和登出 -->
          <div class="flex items-center space-x-3">
            <span class="text-gray-600">{{ username }}</span>
            <button 
              @click="logout" 
              class="text-red-600 hover:underline cursor-pointer"
            >
              登出
            </button>
          </div>
        </div>
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
