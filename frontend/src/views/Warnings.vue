<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 px-4 py-6">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="mb-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
            </svg>
          </div>
          <h1 class="text-2xl font-bold text-gray-900">预警事件</h1>
        </div>
        <p class="text-gray-600">监控和处理系统预警事件，确保及时响应</p>
      </div>

      <!-- 统计卡片 -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div class="bg-white rounded-lg shadow-lg p-4">
          <div class="flex items-center">
            <div class="p-2 bg-red-100 rounded-lg">
              <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">未处理</p>
              <p class="text-2xl font-semibold text-gray-900">{{ getStatusCount('未处理') }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-lg p-4">
          <div class="flex items-center">
            <div class="p-2 bg-yellow-100 rounded-lg">
              <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">已确认</p>
              <p class="text-2xl font-semibold text-gray-900">{{ getStatusCount('已确认') }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-lg p-4">
          <div class="flex items-center">
            <div class="p-2 bg-green-100 rounded-lg">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">已解除</p>
              <p class="text-2xl font-semibold text-gray-900">{{ getStatusCount('已解除') }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-lg p-4">
          <div class="flex items-center">
            <div class="p-2 bg-blue-100 rounded-lg">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">总计</p>
              <p class="text-2xl font-semibold text-gray-900">{{ list.length }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 主内容卡片 -->
      <div class="bg-white rounded-lg shadow-xl p-6">
        <!-- 筛选器 -->
        <div class="bg-gray-50 rounded-lg p-4 mb-6">
          <div class="flex items-center gap-4 flex-wrap">
            <div class="flex items-center gap-2">
              <svg class="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.207A1 1 0 013 6.5V4z"></path>
              </svg>
              <span class="text-sm font-medium text-gray-700">筛选条件：</span>
            </div>
            
            <select 
              v-model="query.status" 
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            >
              <option value="ALL">全部状态</option>
              <option value="未处理">未处理</option>
              <option value="已确认">已确认</option>
              <option value="已解除">已解除</option>
            </select>

            <select 
              v-model="query.pointId" 
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            >
              <option value="">全部监测点</option>
              <option v-for="p in points" :key="p.id" :value="p.id">{{ p.name }}</option>
            </select>

            <button 
              class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors flex items-center gap-2" 
              @click="load"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
              </svg>
              查询
            </button>
          </div>
        </div>

        <!-- 预警事件表格 -->
        <div class="overflow-hidden rounded-lg border border-gray-200">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">事件信息</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">监测点</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警级别</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">触发值</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">触发时间</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="w in list" :key="w.id" class="hover:bg-gray-50 transition-colors">
                <td class="px-4 py-4 whitespace-nowrap">
                  <div class="flex items-center gap-3">
                    <div class="flex-shrink-0">
                      <div class="w-8 h-8 rounded-full flex items-center justify-center"
                           :class="getLevelIconClass(w.level)">
                        <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
                        </svg>
                      </div>
                    </div>
                    <div>
                      <div class="text-sm font-medium text-gray-900">预警事件 #{{ w.id }}</div>
                      <div class="text-sm text-gray-500">{{ formatDate(w.triggerTime) }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <div class="flex items-center gap-2">
                    <div class="w-2 h-2 bg-blue-400 rounded-full"></div>
                    <span class="text-sm font-medium text-gray-900">{{ w.pointName || '未知点' }}</span>
                  </div>
                </td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="getLevelClass(w.level)">
                    {{ getLevelLabel(w.level) }}
                  </span>
                </td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ w.triggerValue }}</div>
                  <div class="text-xs text-gray-500">触发阈值</div>
                </td>
                <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDateTime(w.triggerTime) }}
                </td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="getStatusClass(w.status)">
                    <span class="w-1.5 h-1.5 rounded-full mr-1.5" :class="getStatusDotClass(w.status)"></span>
                    {{ w.status }}
                  </span>
                </td>
                <td class="px-4 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center gap-2 flex-wrap">
                    <button 
                      v-if="w.status === '未处理'"
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-blue-600 bg-blue-100 hover:bg-blue-200 rounded-md transition-colors" 
                      @click="confirm(w)"
                    >
                      <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                      </svg>
                      确认
                    </button>
                    <button 
                      v-if="w.status !== '已解除'"
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-green-600 bg-green-100 hover:bg-green-200 rounded-md transition-colors" 
                      @click="resolve(w)"
                    >
                      <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                      </svg>
                      解除
                    </button>
                    <button 
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-red-600 bg-red-100 hover:bg-red-200 rounded-md transition-colors" 
                      @click="remove(w)"
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'

const list = ref([])
const points = ref([])
const query = reactive({ status: 'ALL', pointId: '' })

// 获取状态统计
const getStatusCount = (status) => {
  if (status === '全部') return list.value.length
  return list.value.filter(item => item.status === status).length
}

// 获取级别样式
const getLevelClass = (level) => {
  const levelMap = {
    1: 'bg-green-100 text-green-800',
    2: 'bg-yellow-100 text-yellow-800', 
    3: 'bg-orange-100 text-orange-800',
    4: 'bg-red-100 text-red-800'
  }
  return levelMap[level] || 'bg-gray-100 text-gray-800'
}

// 获取级别图标样式
const getLevelIconClass = (level) => {
  const levelMap = {
    1: 'bg-green-500',
    2: 'bg-yellow-500', 
    3: 'bg-orange-500',
    4: 'bg-red-500'
  }
  return levelMap[level] || 'bg-gray-500'
}

// 获取级别标签
const getLevelLabel = (level) => {
  const labelMap = {
    1: '1级 轻微',
    2: '2级 一般',
    3: '3级 严重', 
    4: '4级 紧急'
  }
  return labelMap[level] || `L${level}`
}

// 获取状态样式
const getStatusClass = (status) => {
  const statusMap = {
    '未处理': 'bg-red-100 text-red-800',
    '已确认': 'bg-yellow-100 text-yellow-800',
    '已解除': 'bg-green-100 text-green-800'
  }
  return statusMap[status] || 'bg-gray-100 text-gray-800'
}

// 获取状态点样式
const getStatusDotClass = (status) => {
  const statusMap = {
    '未处理': 'bg-red-400',
    '已确认': 'bg-yellow-400',
    '已解除': 'bg-green-400'
  }
  return statusMap[status] || 'bg-gray-400'
}

// 格式化日期（简短版本）
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const today = new Date()
  const diffTime = today - date
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))
  
  if (diffDays === 0) {
    return '今天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  } else if (diffDays === 1) {
    return '昨天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  } else if (diffDays < 7) {
    return `${diffDays}天前`
  } else {
    return date.toLocaleDateString('zh-CN', { month: '2-digit', day: '2-digit' })
  }
}

// 格式化日期时间（完整版本）
const formatDateTime = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const load = async () => {
  try {
    // 加载监测点数据
    const { data: pd } = await request.get('/api/points')
    if (pd.code === 0) points.value = pd.data || []
    
    // 加载预警事件数据
    const { data } = await request.get('/api/warnings', { params: {
        status: query.status === 'ALL' ? undefined : query.status,
        pointId: query.pointId || undefined
      }})
    if (data.code === 0) list.value = data.data || []
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const confirm = async (w) => {
  try {
    const user = localStorage.getItem('username') || 'admin'
    const { data } = await request.put(`/api/warnings/${w.id}/confirm`, null, { params: { user } })
    if (data.code === 0) {
      load()
    } else {
      alert(data.msg || '确认失败')
    }
  } catch (error) {
    console.error('确认失败:', error)
    alert('确认失败，请重试')
  }
}

const resolve = async (w) => {
  try {
    const user = localStorage.getItem('username') || 'admin'
    const { data } = await request.put(`/api/warnings/${w.id}/resolve`, null, { params: { user } })
    if (data.code === 0) {
      load()
    } else {
      alert(data.msg || '解除失败')
    }
  } catch (error) {
    console.error('解除失败:', error)
    alert('解除失败，请重试')
  }
}

const remove = async (w) => {
  if (!confirm(`确认删除预警事件 #${w.id}？删除后不可恢复。`)) return
  
  try {
    const { data } = await request.delete(`/api/warnings/${w.id}`)
    if (data.code === 0) {
      load()
    } else {
      alert(data.msg || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败，请重试')
  }
}

onMounted(load)
</script>
