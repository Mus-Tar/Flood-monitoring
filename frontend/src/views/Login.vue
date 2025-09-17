<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 px-4">
    <div class="max-w-md w-full bg-white rounded-lg shadow-xl p-8">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <div class="w-16 h-16 bg-blue-600 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
          </svg>
        </div>
        <h1 class="text-2xl font-bold text-gray-900">洪水监测系统</h1>
        <p class="text-gray-600 mt-2">请登录以访问系统功能</p>
      </div>

      <!-- 登录表单 -->
      <form @submit.prevent="onSubmit" class="space-y-6">
        <!-- 用户名 -->
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
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
              </svg>
            </div>
          </div>
        </div>

        <!-- 密码 -->
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
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2a3 3 0 11-6 0v-2c0-.83.67-1.5 1.5-1.5h3c.83 0 1.5.67 1.5 1.5z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12V7a4 4 0 00-8 0v5l8 0z"></path>
              </svg>
            </div>
            <button 
              type="button"
              @click="showPassword = !showPassword"
              class="absolute inset-y-0 right-0 pr-3 flex items-center"
            >
              <svg v-if="showPassword" class="w-5 h-5 text-gray-400 hover:text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
              </svg>
              <svg v-else class="w-5 h-5 text-gray-400 hover:text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.878 9.878L3 3m6.878 6.878L21 21"></path>
              </svg>
            </button>
          </div>
        </div>

        <!-- 记住我 -->
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <input 
              id="remember-me" 
              v-model="rememberMe" 
              type="checkbox" 
              class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
            />
            <label for="remember-me" class="ml-2 block text-sm text-gray-700">
              记住我
            </label>
          </div>
        </div>

        <!-- 登录按钮 -->
        <button 
          type="submit" 
          :disabled="isLoading"
          class="w-full bg-blue-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-blue-700 focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
        >
          <span v-if="isLoading">登录中...</span>
          <span v-else>登录</span>
        </button>
      </form>

      <!-- 注册链接 -->
      <div class="text-center mt-6">
        <p class="text-gray-600">
          还没有账号？ 
          <router-link to="/register" class="text-blue-600 hover:text-blue-700 font-medium">
            立即注册
          </router-link>
        </p>
      </div>

      <!-- 演示账号提示 -->
      <div class="mt-6 p-4 bg-gray-50 rounded-lg">
        <h4 class="text-sm font-medium text-gray-700 mb-2">演示账号</h4>
        <div class="text-sm text-gray-600 space-y-1">
          <p><strong>管理员:</strong> admin / admin123</p>
          <p><strong>普通用户:</strong> user / user123</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import request from '../utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()
const isLoading = ref(false)
const showPassword = ref(false)
const rememberMe = ref(false)

// 表单数据
const form = reactive({ 
  username: '', 
  password: '' 
})

// 登录提交
const onSubmit = async () => {
  if (!form.username || !form.password) {
    alert('请输入用户名和密码')
    return
  }

  isLoading.value = true
  
  try {
    // 调用后端登录API
    const { data } = await request.post('/api/auth/login', form)
    
    if (data.code === 0) {
      // 登录成功，保存token和用户信息
      localStorage.setItem('token', data.data.token)
      localStorage.setItem('username', data.data.username)
      localStorage.setItem('userRole', data.data.role || 'USER')
      
      // 记住用户名
      if (rememberMe.value) {
        localStorage.setItem('rememberedUsername', form.username)
      } else {
        localStorage.removeItem('rememberedUsername')
      }
      
      // 跳转到用户管理页面
      router.push('/users')
    } else {
      alert(data.msg || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    if (error.code === 'ECONNREFUSED' || error.message.includes('Network Error')) {
      alert('无法连接到服务器，请检查后端服务是否启动')
    } else {
      alert('用户名或密码错误')
    }
  } finally {
    isLoading.value = false
  }
}

// 组件挂载时加载记住的用户名
onMounted(() => {
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    form.username = rememberedUsername
    rememberMe.value = true
  }
})
</script>

<style scoped>
/* 自定义样式可以在这里添加 */
</style>
