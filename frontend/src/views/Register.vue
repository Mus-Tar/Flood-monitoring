<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
    <div class="max-w-md w-full space-y-8">
      <div class="text-center">
        <div class="mx-auto h-16 w-16 bg-blue-600 rounded-full flex items-center justify-center mb-4">
          <svg class="h-10 w-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
          </svg>
        </div>
        <h2 class="text-3xl font-bold text-gray-900 mb-2">创建新账户</h2>
        <p class="text-gray-600">自助注册只会创建普通用户账号</p>
      </div>

      <div class="bg-white py-8 px-6 shadow-lg rounded-xl border border-gray-100">
        <form class="space-y-6" @submit.prevent="handleRegister">
          <div>
            <label for="username" class="block text-sm font-medium text-gray-700 mb-2">用户名</label>
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
              <div class="absolute inset-y-0 right-0 pr-3 flex items-center">
                <svg v-if="checkingUsername" class="h-5 w-5 text-gray-400 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
                </svg>
                <svg v-else-if="form.username && !usernameError && usernameChecked" class="h-5 w-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
              </div>
            </div>
            <p v-if="usernameError" class="mt-1 text-sm text-red-600">{{ usernameError }}</p>
            <p v-else-if="form.username && usernameChecked" class="mt-1 text-sm text-green-600">用户名可用</p>
          </div>

          <div>
            <label for="password" class="block text-sm font-medium text-gray-700 mb-2">密码</label>
            <input
              id="password"
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              required
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
              placeholder="请输入密码"
            />
            <button type="button" class="mt-2 text-sm text-blue-600" @click="showPassword = !showPassword">
              {{ showPassword ? '隐藏密码' : '显示密码' }}
            </button>
          </div>

          <div>
            <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">确认密码</label>
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
            <button type="button" class="mt-2 text-sm text-blue-600" @click="showConfirmPassword = !showConfirmPassword">
              {{ showConfirmPassword ? '隐藏确认密码' : '显示确认密码' }}
            </button>
            <p v-if="form.confirmPassword && !passwordsMatch" class="mt-1 text-sm text-red-600">两次输入的密码不一致</p>
          </div>

          <div class="rounded-lg bg-blue-50 px-4 py-3 text-sm text-blue-700">
            注册成功后默认角色为 `USER`，管理员账号只能由管理员创建。
          </div>

          <button
            type="submit"
            :disabled="!isFormValid || isLoading"
            class="w-full flex justify-center py-3 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            {{ isLoading ? '注册中...' : '创建账户' }}
          </button>
        </form>

        <div class="text-center mt-6">
          <span class="text-gray-600">已有账号？</span>
          <router-link to="/login" class="font-medium text-blue-600 hover:text-blue-500 ml-1">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

const showPassword = ref(false)
const showConfirmPassword = ref(false)
const isLoading = ref(false)
const checkingUsername = ref(false)
const usernameChecked = ref(false)
const usernameError = ref('')

const passwordsMatch = computed(() => form.value.password === form.value.confirmPassword)

const isFormValid = computed(() => {
  return form.value.username &&
    form.value.password &&
    form.value.confirmPassword &&
    passwordsMatch.value &&
    !usernameError.value &&
    usernameChecked.value
})

let usernameCheckTimer = null

const checkUsername = () => {
  const username = form.value.username.trim()
  usernameError.value = ''
  usernameChecked.value = false

  if (!username) return
  if (username.length < 3) {
    usernameError.value = '用户名至少 3 个字符'
    return
  }

  clearTimeout(usernameCheckTimer)
  usernameCheckTimer = setTimeout(async () => {
    checkingUsername.value = true
    try {
      const { data } = await request.get('/api/auth/check-username', { params: { username } })
      if (data.code === 0) {
        usernameChecked.value = Boolean(data.data?.available)
        usernameError.value = data.data?.available ? '' : '用户名已存在'
      } else {
        usernameError.value = data.msg || '检查失败，请稍后重试'
      }
    } catch (error) {
      console.error('check username failed', error)
      usernameError.value = error.response?.data?.msg || '无法检查用户名'
    } finally {
      checkingUsername.value = false
    }
  }, 300)
}

const handleRegister = async () => {
  if (!isFormValid.value) return

  isLoading.value = true
  try {
    const { data } = await request.post('/api/auth/register', {
      username: form.value.username.trim(),
      password: form.value.password
    })

    if (data.code === 0) {
      alert('注册成功，请登录')
      router.push({
        path: '/login',
        query: { username: form.value.username.trim() }
      })
      return
    }

    alert(data.msg || '注册失败，请重试')
  } catch (error) {
    console.error('register failed', error)
    alert(error.response?.data?.msg || '注册失败，请重试')
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
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
