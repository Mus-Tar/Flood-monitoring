<template>
  <div class="max-w-md mx-auto bg-white p-6 rounded-lg shadow">
    <h2 class="text-xl font-semibold mb-4">登录</h2>
    <form @submit.prevent="onSubmit" class="space-y-4">
      <div>
        <label class="block text-sm mb-1">用户名</label>
        <input v-model="form.username" type="text" class="w-full border rounded px-3 py-2"/>
      </div>
      <div>
        <label class="block text-sm mb-1">密码</label>
        <input v-model="form.password" type="password" class="w-full border rounded px-3 py-2"/>
      </div>
      <button class="bg-blue-600 text-white px-4 py-2 rounded" type="submit">登录</button>
    </form>
    <p class="text-sm text-gray-500 mt-4">演示账号：admin / admin123</p>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import request from '../utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = reactive({ username: '', password: '' })

const onSubmit = async () => {
  const { data } = await request.post('/api/auth/login', form)
  if (data.code === 0) {
    localStorage.setItem('token', data.data.token)
    localStorage.setItem('username', data.data.username)
    router.push('/users')
  } else {
    alert(data.msg || '登录失败')
  }
}
</script>
