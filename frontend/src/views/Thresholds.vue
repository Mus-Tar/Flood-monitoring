<template>
  <div class="bg-white p-4 rounded-lg shadow">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-lg font-semibold">阈值配置</h2>
      <button class="bg-green-600 text-white px-3 py-2 rounded" @click="openAdd">新增配置</button>
    </div>

    <div class="flex items-center gap-3 mb-3">
      <select v-model="query.pointId" class="border rounded px-3 py-2">
        <option value="">全部监测点</option>
        <option v-for="p in points" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
      <select v-model="query.paramType" class="border rounded px-3 py-2">
        <option value="">全部参数</option>
        <option value="WATER_LEVEL">WATER_LEVEL</option>
        <option value="RAINFALL">RAINFALL</option>
      </select>
      <button class="px-3 py-2 bg-gray-100 rounded border" @click="load">查询</button>
    </div>

    <table class="min-w-full border">
      <thead class="bg-gray-100">
      <tr>
        <th class="px-3 py-2 border">ID</th>
        <th class="px-3 py-2 border">监测点</th>
        <th class="px-3 py-2 border">参数</th>
        <th class="px-3 py-2 border">级别</th>
        <th class="px-3 py-2 border">阈值</th>
        <th class="px-3 py-2 border">持续(min)</th>
        <th class="px-3 py-2 border w-40">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="t in list" :key="t.id">
        <td class="px-3 py-2 border">{{ t.id }}</td>
        <td class="px-3 py-2 border">{{ pointName(t.pointId) }}</td>
        <td class="px-3 py-2 border">{{ t.paramType }}</td>
        <td class="px-3 py-2 border">{{ t.level }}</td>
        <td class="px-3 py-2 border">{{ t.thresholdValue }}</td>
        <td class="px-3 py-2 border">{{ t.duration }}</td>
        <td class="px-3 py-2 border space-x-2">
          <button class="px-2 py-1 text-white bg-blue-600 rounded" @click="openEdit(t)">编辑</button>
          <button class="px-2 py-1 text-white bg-red-600 rounded" @click="remove(t)">删除</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- 弹窗 -->
    <div v-if="show" class="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div class="bg-white p-6 rounded-lg w-[520px]">
        <h3 class="text-lg font-semibold mb-3">{{ form.id ? '编辑配置' : '新增配置' }}</h3>
        <div class="space-y-3">
          <div>
            <label class="block text-sm mb-1">监测点</label>
            <select v-model="form.pointId" class="w-full border rounded px-3 py-2">
              <option v-for="p in points" :key="p.id" :value="p.id">{{ p.name }}</option>
            </select>
          </div>
          <div class="flex gap-3">
            <div class="flex-1">
              <label class="block text-sm mb-1">参数</label>
              <select v-model="form.paramType" class="w-full border rounded px-3 py-2">
                <option value="WATER_LEVEL">WATER_LEVEL</option>
                <option value="RAINFALL">RAINFALL</option>
              </select>
            </div>
            <div class="flex-1">
              <label class="block text-sm mb-1">级别(1-4)</label>
              <input v-model.number="form.level" type="number" min="1" max="4" class="w-full border rounded px-3 py-2"/>
            </div>
          </div>
          <div class="flex gap-3">
            <div class="flex-1">
              <label class="block text-sm mb-1">阈值</label>
              <input v-model.number="form.thresholdValue" type="number" step="0.01" class="w-full border rounded px-3 py-2"/>
            </div>
            <div class="flex-1">
              <label class="block text-sm mb-1">持续(min)</label>
              <input v-model.number="form.duration" type="number" min="0" class="w-full border rounded px-3 py-2"/>
            </div>
          </div>
        </div>
        <div class="mt-4 flex justify-end gap-2">
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
const points = ref([])
const show = ref(false)
const query = reactive({ pointId: '', paramType: '' })
const form = reactive({ id: null, pointId: '', paramType: 'WATER_LEVEL', level: 1, thresholdValue: 0, duration: 0 })

const pointName = (id) => points.value.find(p => p.id === id)?.name || ('#' + id)

const load = async () => {
  const { data: pd } = await request.get('/api/points')
  if (pd.code === 0) points.value = pd.data || []
  const { data } = await request.get('/api/thresholds', { params: {
      pointId: query.pointId || undefined,
      paramType: query.paramType || undefined
    }})
  if (data.code === 0) list.value = data.data || []
}

const openAdd = () => { Object.assign(form, { id: null, pointId: points.value[0]?.id || '', paramType: 'WATER_LEVEL', level: 1, thresholdValue: 0, duration: 0 }); show.value = true }
const openEdit = (t) => { Object.assign(form, t); show.value = true }

const save = async () => {
  if (!form.pointId) { alert('请选择监测点'); return }
  if (form.id) {
    const { data } = await request.put('/api/thresholds/' + form.id, form)
    if (data.code === 0) { show.value = false; load() }
  } else {
    const { data } = await request.post('/api/thresholds', form)
    if (data.code === 0) { show.value = false; load() }
  }
}
const remove = async (t) => {
  if (!confirm('确认删除该阈值配置？')) return
  const { data } = await request.delete('/api/thresholds/' + t.id)
  if (data.code === 0) load()
}

onMounted(load)
</script>
