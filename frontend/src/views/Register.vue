<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
    <div class="max-w-md w-full space-y-8">
      <!-- 头部 -->
      <div class="text-center">
        <div class="mx-auto h-16 w-16 bg-blue-600 rounded-full flex items-center justify-center mb-4">
          <svg class="h-10 w-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z"></path>
          </svg>
        </div>
        <h2 class="text-3xl font-bold text-gray-900 mb-2">创建新账户</h2>
        <p class="text-gray-600">加入洪水监测系统，开始您的工作</p>
      </div>

      <!-- 注册表单 -->
      <div class="bg-white py-8 px-6 shadow-lg rounded-xl border border-gray-100">
        <form class="space-y-6" @submit.prevent="handleRegister">
          <!-- 用户名 -->
          <div>
            <label for="username" class="block text-sm font-medium text-gray-700 mb-2">
              用户名
            </label>
            <div class="relative">
              <input
                id="username"
                v-model="form.username"
                type="text"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                :class="{
                  'border-red-500': usernameError,
                  'border-green-500': form.username && !usernameError && usernameChecked
                }"
                placeholder="请输入用户名"
                @input="checkUsername"
              />
              <!-- 用户名验证状态 -->
              <div class="absolute inset-y-0 right-0 pr-3 flex items-center">
                <svg v-if="checkingUsername" class="h-5 w-5 text-gray-400 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <svg v-else-if="form.username && !usernameError && usernameChecked" class="h-5 w-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <svg v-else-if="usernameError" class="h-5 w-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </div>
            </div>
            <p v-if="usernameError" class="mt-1 text-sm text-red-600">{{ usernameError }}</p>
            <p v-else-if="form.username && usernameChecked" class="mt-1 text-sm text-green-600">用户名可用</p>
          </div>

          <!-- 密码 -->
          <div>
            <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
              密码
            </label>
            <div class="relative">
              <input
                id="password"
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors pr-10"
                placeholder="请输入密码"
              />
              <button
                type="button"
                class="absolute inset-y-0 right-0 pr-3 flex items-center"
                @click="showPassword = !showPassword"
              >
                <svg v-if="showPassword" class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.878 9.878L3 3m6.878 6.878L21 21"></path>
                </svg>
                <svg v-else class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
                </svg>
              </button>
            </div>
            <!-- 密码强度指示器 -->
            <div v-if="form.password" class="mt-2">
              <div class="flex items-center space-x-2 text-xs">
                <span class="text-gray-600">密码强度:</span>
                <div class="flex space-x-1">
                  <div class="h-2 w-6 rounded-full" :class="passwordStrength >= 1 ? 'bg-red-400' : 'bg-gray-200'"></div>
                  <div class="h-2 w-6 rounded-full" :class="passwordStrength >= 2 ? 'bg-yellow-400' : 'bg-gray-200'"></div>
                  <div class="h-2 w-6 rounded-full" :class="passwordStrength >= 3 ? 'bg-blue-400' : 'bg-gray-200'"></div>
                  <div class="h-2 w-6 rounded-full" :class="passwordStrength >= 4 ? 'bg-green-400' : 'bg-gray-200'"></div>
                </div>
                <span :class="{
                  'text-red-600': passwordStrength === 1,
                  'text-yellow-600': passwordStrength === 2,
                  'text-blue-600': passwordStrength === 3,
                  'text-green-600': passwordStrength === 4
                }">
                  {{ passwordStrengthText }}
                </span>
              </div>
            </div>
          </div>

          <!-- 确认密码 -->
          <div>
            <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">
              确认密码
            </label>
            <input
              id="confirmPassword"
              v-model="form.confirmPassword"
              :type="showConfirmPassword ? 'text' : 'password'"
              required
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
              :class="{
                'border-red-500': form.confirmPassword && !passwordsMatch,
                'border-green-500': form.confirmPassword && passwordsMatch
              }"
              placeholder="请再次输入密码"
            />
            <p v-if="form.confirmPassword && !passwordsMatch" class="mt-1 text-sm text-red-600">密码不一致</p>
          </div>

          <!-- 角色选择 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">角色</label>
            <div class="grid grid-cols-2 gap-4">
              <label class="relative">
                <input
                  v-model="form.role"
                  type="radio"
                  value="user"
                  class="sr-only"
                />
                <div class="p-4 border-2 rounded-lg cursor-pointer transition-all" :class="form.role === 'user' ? 'border-blue-500 bg-blue-50' : 'border-gray-300 hover:border-gray-400'">
                  <div class="flex items-center">
                    <svg class="h-8 w-8 text-blue-600 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    <div>
                      <div class="font-medium">普通用户</div>
                      <div class="text-sm text-gray-600">基础监测功能</div>
                    </div>
                  </div>
                </div>
              </label>
              <label class="relative">
                <input
                  v-model="form.role"
                  type="radio"
                  value="admin"
                  class="sr-only"
                />
                <div class="p-4 border-2 rounded-lg cursor-pointer transition-all" :class="form.role === 'admin' ? 'border-blue-500 bg-blue-50' : 'border-gray-300 hover:border-gray-400'">
                  <div class="flex items-center">
                    <svg class="h-8 w-8 text-purple-600 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path>
                    </svg>
                    <div>
                      <div class="font-medium">管理员</div>
                      <div class="text-sm text-gray-600">全部管理权限</div>
                    </div>
                  </div>
                </div>
              </label>
            </div>
          </div>

          <!-- 提交按钮 -->
          <button
            type="submit"
            :disabled="!isFormValid || isLoading"
            class="w-full flex justify-center py-3 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            <svg v-if="isLoading" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            {{ isLoading ? '注册中...' : '创建账户' }}
          </button>
        </form>

        <!-- 登录链接 -->
        <div class="text-center mt-6">
          <span class="text-gray-600">已有账户？</span>
          <router-link
            to="/login"
            class="font-medium text-blue-600 hover:text-blue-500 ml-1"
          >
            立即登录
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 表单数据
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  role: 'user'
})

// 状态
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const isLoading = ref(false)
const checkingUsername = ref(false)
const usernameChecked = ref(false)
const usernameError = ref('')

// 计算属性
const passwordsMatch = computed(() => {
  return form.value.password === form.value.confirmPassword
})

const passwordStrength = computed(() => {
  const password = form.value.password
  if (password.length < 6) return 1
  
  let strength = 1
  if (password.length >= 8) strength++
  if (/[A-Z]/.test(password)) strength++
  if (/[0-9]/.test(password)) strength++
  if (/[^A-Za-z0-9]/.test(password)) strength++
  
  return Math.min(strength, 4)
})

const passwordStrengthText = computed(() => {
  const texts = ['', '弱', '中', '强', '很强']
  return texts[passwordStrength.value] || ''
})

const isFormValid = computed(() => {
  return form.value.username &&
         form.value.password &&
         form.value.confirmPassword &&
         passwordsMatch.value &&
         !usernameError.value &&
         usernameChecked.value
})

// 用户名检查
const checkUsername = async () => {
  const username = form.value.username
  if (!username) {
    usernameError.value = ''
    usernameChecked.value = false
    return
  }

  if (username.length < 3) {
    usernameError.value = '用户名至少3个字符'
    usernameChecked.value = false
    return
  }

  checkingUsername.value = true
  
  // 模拟API检查
  setTimeout(() => {
    // 检查已存在的用户名（从localStorage获取）
    const registeredUsers = JSON.parse(localStorage.getItem('registeredUsers') || '{}')
    if (registeredUsers[username]) {
      usernameError.value = '用户名已存在'
      usernameChecked.value = false
    } else {
      usernameError.value = ''
      usernameChecked.value = true
    }
    checkingUsername.value = false
  }, 500)
}

// 注册处理
const handleRegister = async () => {
  if (!isFormValid.value) return

  isLoading.value = true
  
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 保存用户到localStorage
    const registeredUsers = JSON.parse(localStorage.getItem('registeredUsers') || '{}')
    registeredUsers[form.value.username] = {
      password: form.value.password,
      role: form.value.role,
      createdAt: new Date().toISOString()
    }
    localStorage.setItem('registeredUsers', JSON.stringify(registeredUsers))
    
    // 注册成功，跳转到登录页
    router.push({ 
      path: '/login',
      query: { message: '注册成功，请登录', username: form.value.username }
    })
  } catch (error) {
    console.error('注册失败:', error)
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
/* 自定义样式 */
.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>