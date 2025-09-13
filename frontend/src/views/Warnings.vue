<template>
  <div class="bg-white p-4 rounded-lg shadow">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-lg font-semibold">预警事件</h2>
    </div>

    <div class="flex items-center gap-3 mb-3">
      <select v-model="query.status" class="border rounded px-3 py-2">
        <option value="ALL">全部状态</option>
        <option value="未处理">未处理</option>
        <option value="已确认">已确认</option>
        <option value="已解除">已解除</option>
      </select>

      <select v-model="query.pointId" class="border rounded px-3 py-2">
        <option value="">全部监测点</option>
        <option v-for="p in points" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>

      <button class="px-3 py-2 bg-gray-100 rounded border" @click="load">查询</button>
    </div>

    <table class="min-w-full border">
      <thead class="bg-gray-100">
      <tr>
        <th class="px-3 py-2 border">ID</th>
        <th class="px-3 py-2 border">监测点</th>
        <th class="px-3 py-2 border">级别</th>
        <th class="px-3 py-2 border">触发值</th>
        <th class="px-3 py-2 border">触发时间</th>
        <th class="px-3 py-2 border">状态</th>
        <th class="px-3 py-2 border w-[22rem]">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="w in list" :key="w.id">
        <td class="px-3 py-2 border">{{ w.id }}</td>
        <td class="px-3 py-2 border">{{ w.pointName }}</td>
        <td class="px-3 py-2 border">L{{ w.level }}</td>
        <td class="px-3 py-2 border">{{ w.triggerValue }}</td>
        <td class="px-3 py-2 border">{{ w.triggerTime }}</td>
        <td class="px-3 py-2 border">{{ w.status }}</td>
        <td class="px-3 py-2 border space-x-2">
          <button class="px-2 py-1 text-white bg-blue-600 rounded" @click="confirm(w)" :disabled="w.status!=='未处理'">确认</button>
          <button class="px-2 py-1 text-white bg-green-600 rounded" @click="resolve(w)" :disabled="w.status==='已解除'">解除</button>
          <button class="px-2 py-1 text-white bg-red-600 rounded" @click="remove(w)">删除</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'

const list = ref([])
const points = ref([])
const query = reactive({ status: 'ALL', pointId: '' })

const load = async () => {
  const { data: pd } = await request.get('/api/points')
  if (pd.code === 0) points.value = pd.data || []
  const { data } = await request.get('/api/warnings', { params: {
      status: query.status || undefined,
      pointId: query.pointId || undefined
    }})
  if (data.code === 0) list.value = data.data || []
}

const confirm = async (w) => {
  const user = localStorage.getItem('username') || 'admin'
  const { data } = await request.put(`/api/warnings/${w.id}/confirm`, null, { params: { user } })
  if (data.code === 0) load()
}
const resolve = async (w) => {
  const user = localStorage.getItem('username') || 'admin'
  const { data } = await request.put(`/api/warnings/${w.id}/resolve`, null, { params: { user } })
  if (data.code === 0) load()
}
const remove = async (w) => {
  if (!confirm('确认删除该预警事件？删除后不可恢复。')) return
  const { data } = await request.delete(`/api/warnings/${w.id}`)
  if (data.code === 0) load()
}

onMounted(load)
</script>
