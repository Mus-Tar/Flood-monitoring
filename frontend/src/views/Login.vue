<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 px-4">
    <div class="max-w-md w-full bg-white rounded-lg shadow-xl p-8">
      <div class="text-center mb-8">
        <div class="w-16 h-16 bg-blue-600 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <h1 class="text-2xl font-bold text-gray-900">智慧水务洪水监测预警系统</h1>
        <p class="text-gray-600 mt-2">请登录以访问系统功能</p>
      </div>

      <form @submit.prevent="onSubmit" class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">用户名</label>
          <div class="relative">
            <input
              v-model="form.username"
              type="text"
              required
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors pl-12"
              placeholder="请输入用户名"
            />
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">密码</label>
          <div class="relative">
            <input
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              required
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors pl-12 pr-12"
              placeholder="请输入密码"
            />
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2a3 3 0 11-6 0v-2c0-.83.67-1.5 1.5-1.5h3c.83 0 1.5.67 1.5 1.5z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12V7a4 4 0 00-8 0v5l8 0z" />
              </svg>
            </div>
            <button type="button" @click="showPassword = !showPassword" class="absolute inset-y-0 right-0 pr-3 flex items-center">
              <span class="text-sm text-gray-500">{{ showPassword ? '隐藏' : '显示' }}</span>
            </button>
          </div>
        </div>

        <div class="flex items-center">
          <input id="remember-me" v-model="rememberMe" type="checkbox" class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
          <label for="remember-me" class="ml-2 block text-sm text-gray-700">记住用户名</label>
        </div>

        <button
          type="submit"
          :disabled="isLoading"
          class="w-full bg-blue-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-blue-700 focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
        >
          {{ isLoading ? '登录中...' : '登录' }}
        </button>
      </form>

      <div class="text-center mt-6">
        <p class="text-gray-600">
          还没有账号？
          <router-link to="/register" class="text-blue-600 hover:text-blue-700 font-medium">立即注册</router-link>
        </p>
      </div>

      <div class="mt-6 p-4 bg-gray-50 rounded-lg">
        <h4 class="text-sm font-medium text-gray-700 mb-2">演示账号</h4>
        <div class="text-sm text-gray-600 space-y-1">
          <p><strong>管理员</strong> admin / admin123</p>
          <p><strong>普通用户</strong> user / user123</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'
import { setAuthData } from '../utils/auth'

const route = useRoute()
const router = useRouter()
const isLoading = ref(false)
const showPassword = ref(false)
const rememberMe = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const onSubmit = async () => {
  if (!form.username || !form.password) {
    alert('请输入用户名和密码')
    return
  }

  isLoading.value = true
  try {
    const { data } = await request.post('/api/auth/login', form)
    if (data.code !== 0 || !data.data?.accessToken) {
      alert(data.msg || '登录失败')
      return
    }

    setAuthData(data.data)

    if (rememberMe.value) {
      localStorage.setItem('rememberedUsername', form.username)
    } else {
      localStorage.removeItem('rememberedUsername')
    }

    router.push(data.data.userInfo?.role === 'ADMIN' ? '/users' : '/warnings')
  } catch (error) {
    console.error('login failed', error)
    if (error.response?.data?.msg) {
      alert(error.response.data.msg)
    } else if (error.code === 'ERR_NETWORK' || String(error.message || '').includes('Network Error')) {
      alert('无法连接后端服务，请确认 8080 端口的 Spring Boot 服务已启动')
    } else if ((error.response?.status || 0) >= 500) {
      alert('后端服务发生异常，请查看 Spring Boot 控制台日志')
    } else {
      alert('登录失败，请打开浏览器开发者工具查看 /api/auth/login 的响应内容')
    }
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  const rememberedUsername = route.query.username || localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    form.username = rememberedUsername
    rememberMe.value = true
  }
})
</script>
