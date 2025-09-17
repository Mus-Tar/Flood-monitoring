<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 px-4 py-6">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="mb-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
            </svg>
          </div>
          <h1 class="text-2xl font-bold text-gray-900">监测点管理</h1>
        </div>
        <p class="text-gray-600">管理洪水监测系统中的各个监测点配置</p>
      </div>

      <!-- 主内容卡片 -->
      <div class="bg-white rounded-lg shadow-xl p-6">
        <!-- 搜索和操作栏 -->
        <div class="flex items-center justify-between mb-6">
          <div class="flex items-center gap-4">
            <div class="relative">
              <input 
                v-model="keyword" 
                placeholder="搜索名称/类型/位置/流域/型号" 
                class="w-80 px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors pl-12"
                @keyup.enter="load"
              />
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                </svg>
              </div>
            </div>
            <button 
              class="px-4 py-3 bg-gray-100 hover:bg-gray-200 rounded-lg border transition-colors flex items-center gap-2" 
              @click="load"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
              </svg>
              搜索
            </button>
          </div>
          <button 
            class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-3 rounded-lg transition-colors flex items-center gap-2 shadow-sm" 
            @click="openAdd"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
            </svg>
            新增监测点
          </button>
        </div>

        <!-- 监测点表格 -->
        <div class="overflow-hidden rounded-lg border border-gray-200">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">名称</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">位置</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">流域</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">安装高度(m)</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">传感器型号</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="p in list" :key="p.id" class="hover:bg-gray-50 transition-colors">
                <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-900">{{ p.id }}</td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ p.name }}</div>
                </td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium" 
                        :class="getTypeClass(p.type)">
                    {{ getTypeLabel(p.type) }}
                  </span>
                </td>
                <td class="px-4 py-4 text-sm text-gray-900">{{ p.location }}</td>
                <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-900">{{ p.riverBasin }}</td>
                <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-900">{{ p.installHeight }}</td>
                <td class="px-4 py-4 text-sm text-gray-900">{{ p.sensorModel }}</td>
                <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ p.createdAt ? formatDate(p.createdAt) : '-' }}
                </td>
                <td class="px-4 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center gap-2">
                    <button 
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-blue-600 bg-blue-100 hover:bg-blue-200 rounded-md transition-colors" 
                      @click="openEdit(p)"
                    >
                      <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                      </svg>
                      编辑
                    </button>
                    <button 
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-red-600 bg-red-100 hover:bg-red-200 rounded-md transition-colors" 
                      @click="remove(p)"
                    >
                      <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                      </svg>
                      删除
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>
    </div>

    <!-- 弹窗表单 -->
    <div v-if="show" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4">
      <div class="bg-white rounded-lg shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <!-- 弹窗头部 -->
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center gap-3">
            <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
              <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900">{{ form.id ? '编辑监测点' : '新增监测点' }}</h3>
          </div>
        </div>
        
        <!-- 弹窗内容 -->
        <div class="px-6 py-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">名称</label>
              <input 
                v-model="form.name" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors" 
                placeholder="请输入监测点名称"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">类型</label>
              <select 
                v-model="form.type" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors bg-white"
              >
                <option value="">请选择监测点类型</option>
                <option v-for="type in monitoringPointTypes" :key="type.value" :value="type.value">
                  {{ type.label }}
                </option>
              </select>
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-2">位置（经纬度或区域）</label>
              <input 
                v-model="form.location" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors" 
                placeholder="请输入具体位置信息"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">所属流域/河流</label>
              <input 
                v-model="form.riverBasin" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors" 
                placeholder="请输入流域或河流名称"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">安装高度(m)</label>
              <input 
                v-model.number="form.installHeight" 
                type="number" 
                step="0.01" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors" 
                placeholder="0.00"
              />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-2">传感器型号</label>
              <input 
                v-model="form.sensorModel" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors" 
                placeholder="请输入传感器型号"
              />
            </div>
          </div>
        </div>
        
        <!-- 弹窗底部 -->
        <div class="px-6 py-4 border-t border-gray-200 flex justify-end gap-3">
          <button 
            class="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors" 
            @click="show=false"
          >
            取消
          </button>
          <button 
            class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors flex items-center gap-2" 
            @click="save"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
            </svg>
            保存
          </button>
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

// 监测点类型定义 - 与业务系统保持一致
const monitoringPointTypes = [
  { value: 'WATER_LEVEL', label: '水位监测' },
  { value: 'RAINFALL', label: '雨量监测' }
]

// 获取监测点类型样式
const getTypeClass = (type) => {
  const typeMap = {
    'WATER_LEVEL': 'bg-green-100 text-green-800',
    'RAINFALL': 'bg-blue-100 text-blue-800'
  }
  return typeMap[type] || 'bg-gray-100 text-gray-800'
}

// 获取监测点类型标签
const getTypeLabel = (type) => {
  const labelMap = {
    'WATER_LEVEL': '水位监测',
    'RAINFALL': '雨量监测'
  }
  return labelMap[type] || type
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const load = async () => {
  try {
    const { data } = await request.get('/api/points', { params: { keyword: keyword.value || undefined }})
    if(data.code === 0) {
      // 按id从小到大排序
      list.value = (data.data || []).sort((a, b) => a.id - b.id)
    }
  } catch (error) {
    console.error('加载监测点失败:', error)
    // 可以添加错误提示
  }
}

const openAdd = () => { Object.assign(form, blank); show.value = true }
const openEdit = (p) => { Object.assign(form, p); show.value = true }

const save = async () => {
  try {
    if (form.id) {
      const { data } = await request.put('/api/points/' + form.id, form)
      if (data.code === 0) { show.value = false; load() }
    } else {
      const { data } = await request.post('/api/points', form)
      if (data.code === 0) { show.value = false; load() }
    }
  } catch (error) {
    console.error('保存失败:', error)
    // 可以添加错误提示
  }
}

const remove = async (p) => {
  if (!confirm('确认删除该监测点？将一并删除其监测数据。')) return
  try {
    const { data } = await request.delete('/api/points/' + p.id)
    if (data.code === 0) load()
  } catch (error) {
    console.error('删除失败:', error)
    // 可以添加错误提示
  }
}

onMounted(load)
</script>
