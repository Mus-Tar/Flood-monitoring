<template>
  <div class="bg-white p-4 rounded-lg shadow">
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center gap-2">
        <h2 class="text-lg font-semibold">监测点管理</h2>
        <input v-model="keyword" placeholder="搜索名称/类型/位置/流域/型号" class="border rounded px-3 py-2" @keyup.enter="load"/>
        <button class="px-3 py-2 bg-gray-100 rounded border" @click="load">搜索</button>
      </div>
      <button class="bg-green-600 text-white px-3 py-2 rounded" @click="openAdd">新增监测点</button>
    </div>

    <table class="min-w-full border">
      <thead class="bg-gray-100">
      <tr>
        <th class="px-3 py-2 border">ID</th>
        <th class="px-3 py-2 border">名称</th>
        <th class="px-3 py-2 border">类型</th>
        <th class="px-3 py-2 border">位置</th>
        <th class="px-3 py-2 border">流域</th>
        <th class="px-3 py-2 border">安装高度(m)</th>
        <th class="px-3 py-2 border">传感器型号</th>
        <th class="px-3 py-2 border">创建时间</th>
        <th class="px-3 py-2 border w-40">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="p in list" :key="p.id" class="border-b">
        <td class="px-3 py-2 border">{{ p.id }}</td>
        <td class="px-3 py-2 border">{{ p.name }}</td>
        <td class="px-3 py-2 border">{{ p.type }}</td>
        <td class="px-3 py-2 border">{{ p.location }}</td>
        <td class="px-3 py-2 border">{{ p.riverBasin }}</td>
        <td class="px-3 py-2 border">{{ p.installHeight }}</td>
        <td class="px-3 py-2 border">{{ p.sensorModel }}</td>
        <td class="px-3 py-2 border">{{ p.createdAt ?? '-' }}</td>
        <td class="px-3 py-2 border space-x-2">
          <button class="px-2 py-1 text-white bg-blue-600 rounded" @click="openEdit(p)">编辑</button>
          <button class="px-2 py-1 text-white bg-red-600 rounded" @click="remove(p)">删除</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- 弹窗表单 -->
    <div v-if="show" class="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div class="bg-white p-6 rounded-lg w-[620px]">
        <h3 class="text-lg font-semibold mb-3">{{ form.id ? '编辑监测点' : '新增监测点' }}</h3>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="block text-sm mb-1">名称</label>
            <input v-model="form.name" class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm mb-1">类型</label>
            <input v-model="form.type" class="w-full border rounded px-3 py-2" placeholder="如：RAIN/GATE/WATER_LEVEL"/>
          </div>
          <div class="col-span-2">
            <label class="block text-sm mb-1">位置（经纬度或区域）</label>
            <input v-model="form.location" class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm mb-1">所属流域/河流</label>
            <input v-model="form.riverBasin" class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm mb-1">安装高度(m)</label>
            <input v-model.number="form.installHeight" type="number" step="0.01" class="w-full border rounded px-3 py-2" />
          </div>
          <div class="col-span-2">
            <label class="block text-sm mb-1">传感器型号</label>
            <input v-model="form.sensorModel" class="w-full border rounded px-3 py-2" />
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
const keyword = ref('')
const blank = { id: null, name: '', type: '', location: '', riverBasin: '', installHeight: null, sensorModel: '' }
const form = reactive({ ...blank })

const load = async () => {
  const { data } = await request.get('/api/points', { params: { keyword: keyword.value || undefined }})
  if(data.code === 0) list.value = data.data || []
}

const openAdd = () => { Object.assign(form, blank); show.value = true }
const openEdit = (p) => { Object.assign(form, p); show.value = true }

const save = async () => {
  if (form.id) {
    const { data } = await request.put('/api/points/' + form.id, form)
    if (data.code === 0) { show.value = false; load() }
  } else {
    const { data } = await request.post('/api/points', form)
    if (data.code === 0) { show.value = false; load() }
  }
}

const remove = async (p) => {
  if (!confirm('确认删除该监测点？将一并删除其监测数据。')) return
  const { data } = await request.delete('/api/points/' + p.id)
  if (data.code === 0) load()
}

onMounted(load)
</script>
