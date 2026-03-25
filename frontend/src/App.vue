<template>
  <div class="min-h-screen">
    <header v-if="isLoggedIn" class="bg-gradient-to-r from-blue-600 via-blue-700 to-indigo-800 shadow-xl">
      <div class="mx-auto max-w-7xl px-4 py-3">
        <div class="flex items-center justify-between mb-3">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 bg-white/20 rounded-xl flex items-center justify-center backdrop-blur-sm">
              <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.002 4.002 0 003 15z" />
              </svg>
            </div>
            <div>
              <h1 class="text-xl font-bold text-white">智慧水务洪水监测预警系统</h1>
            </div>
          </div>

          <div class="flex items-center gap-4">
            <div class="flex items-center gap-3 px-4 py-2 bg-white/10 rounded-lg backdrop-blur-sm">
              <div class="w-8 h-8 bg-gradient-to-r from-blue-400 to-cyan-500 rounded-full flex items-center justify-center">
                <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </div>
              <span class="text-white font-medium">{{ username }}</span>
            </div>
            <button
              @click="logout"
              class="px-4 py-2 bg-red-500/80 hover:bg-red-600 text-white rounded-lg transition-all duration-200 backdrop-blur-sm hover:shadow-lg"
            >
              退出
            </button>
          </div>
        </div>

        <nav class="flex items-center gap-2">
          <div class="flex items-center gap-1" v-if="isAdmin()">
            <router-link to="/points" class="nav-link" :class="{ active: $route.path === '/points' }">监测点管理</router-link>
            <router-link to="/import" class="nav-link" :class="{ active: $route.path === '/import' }">数据导入</router-link>
          </div>

          <div class="w-px h-6 bg-blue-400/30 mx-2" v-if="isAdmin()"></div>

          <div class="flex items-center gap-1" v-if="isAdmin()">
            <router-link to="/thresholds" class="nav-link" :class="{ active: $route.path === '/thresholds' }">阈值配置</router-link>
            <router-link to="/users" class="nav-link" :class="{ active: $route.path === '/users' }">用户管理</router-link>
          </div>

          <div class="w-px h-6 bg-blue-400/30 mx-2" v-if="isAdmin()"></div>

          <div class="flex items-center gap-1">
            <router-link to="/warnings" class="nav-link" :class="{ active: $route.path === '/warnings' }">预警事件</router-link>
            <router-link to="/gis" class="nav-link" :class="{ active: $route.path === '/gis' }">GIS地图</router-link>
          </div>

          <div class="w-px h-6 bg-blue-400/30 mx-2"></div>

          <div class="flex items-center gap-1">
            <router-link to="/history" class="nav-link" :class="{ active: $route.path === '/history' }">历史数据</router-link>
            <router-link to="/forecast" class="nav-link" :class="{ active: $route.path === '/forecast' }">预测工作台</router-link>
          </div>
        </nav>
      </div>
    </header>

    <main :class="isLoggedIn ? 'mx-auto max-w-7xl p-4' : ''">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from './utils/request'
import { clearAuthData, getAccessToken, getRefreshToken, getUserInfo } from './utils/auth'

const router = useRouter()
const route = useRoute()

const username = ref('')
const role = ref('USER')

const syncUserState = () => {
  const userInfo = getUserInfo()
  username.value = userInfo?.username || ''
  role.value = userInfo?.role || 'USER'
}

const isLoggedIn = computed(() => Boolean(getAccessToken()))
const isAdmin = () => role.value === 'ADMIN'

const logout = async () => {
  const refreshToken = getRefreshToken()
  try {
    if (refreshToken) {
      await request.post('/api/auth/logout', { refreshToken })
    }
  } catch (error) {
    console.error('logout failed', error)
  } finally {
    clearAuthData()
    syncUserState()
    router.push('/login')
  }
}

watch(() => route.fullPath, syncUserState)

onMounted(syncUserState)
</script>

<style scoped>
.nav-link {
  @apply px-4 py-2 text-blue-100 hover:text-white hover:bg-white/10 rounded-lg transition-all duration-200 backdrop-blur-sm;
}

.nav-link.active {
  @apply bg-white/20 text-white shadow-lg;
}
</style>
