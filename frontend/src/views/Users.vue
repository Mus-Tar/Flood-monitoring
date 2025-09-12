<template>
  <div class="bg-white p-4 rounded-lg shadow">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-lg font-semibold">用户管理</h2>
      <button class="bg-green-600 text-white px-3 py-2 rounded" @click="openAdd">新增用户</button>
    </div>

    <table class="min-w-full border">
      <thead class="bg-gray-100">
        <tr>
          <th class="px-3 py-2 border">ID</th>
          <th class="px-3 py-2 border">用户名</th>
          <th class="px-3 py-2 border">角色</th>
          <th class="px-3 py-2 border w-40">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="u in list" :key="u.id" class="border-b">
          <td class="px-3 py-2 border">{{ u.id }}</td>
          <td class="px-3 py-2 border">{{ u.username }}</td>
          <td class="px-3 py-2 border">{{ u.role }}</td>
          <td class="px-3 py-2 border space-x-2">
            <button class="px-2 py-1 text-white bg-blue-600 rounded" @click="openEdit(u)">编辑</button>
            <button class="px-2 py-1 text-white bg-red-600 rounded" @click="remove(u)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="show" class="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div class="bg-white p-6 rounded-lg w-[420px]">
        <h3 class="text-lg font-semibold mb-3">{{ form.id ? '编辑用户' : '新增用户' }}</h3>
        <div class="space-y-3">
          <div>
            <label class="block text-sm mb-1">用户名</label>
            <input v-model="form.username" class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm mb-1">密码</label>
            <input v-model="form.password" type="password" class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm mb-1">角色</label>
            <input v-model="form.role" class="w-full border rounded px-3 py-2" placeholder="ADMIN/USER"/>
          </div>
        </div>
        <div class="mt-4 flex justify-end space-x-2">
          <button class="px-3 py-2" @click="show=false">取消</button>
          <button class="px-3 py-2 bg-blue-600 text-white rounded" @click="save">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'

const list = ref([])
const show = ref(false)
const form = reactive({ id: null, username: '', password: '', role: 'ADMIN' })

const load = async () => {
  const { data } = await request.get('/api/users')
  if(data.code === 0) list.value = data.data || []
}

const openAdd = () => {
  Object.assign(form, { id: null, username: '', password: '', role: 'ADMIN' })
  show.value = true
}
const openEdit = (u) => {
  Object.assign(form, u)
  show.value = true
}
const save = async () => {
  if (form.id) {
    const { data } = await request.put('/api/users/' + form.id, form)
    if (data.code === 0) { show.value = false; load() }
  } else {
    const { data } = await request.post('/api/users', form)
    if (data.code === 0) { show.value = false; load() }
  }
}
const remove = async (u) => {
  if (!confirm('确认删除该用户？')) return
  const { data } = await request.delete('/api/users/' + u.id)
  if (data.code === 0) load()
}

onMounted(load)
</script>
