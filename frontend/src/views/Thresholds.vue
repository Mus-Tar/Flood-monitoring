<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 px-4 py-6">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="mb-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
          </div>
          <h1 class="text-2xl font-bold text-gray-900">阈值配置</h1>
        </div>
        <p class="text-gray-600">配置各监测点的告警阈值，实现智能预警</p>
      </div>

      <!-- 主内容卡片 -->
      <div class="bg-white rounded-lg shadow-xl p-6">
        <!-- 操作栏 -->
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-lg font-semibold text-gray-900">阈值管理</h2>
          <button 
            class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-3 rounded-lg transition-colors flex items-center gap-2 shadow-sm" 
            @click="openAdd"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
            </svg>
            新增配置
          </button>
        </div>

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
              v-model="query.pointId" 
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            >
              <option value="">全部监测点</option>
              <option v-for="p in points" :key="p.id" :value="p.id">{{ p.name }}</option>
            </select>
            <select 
              v-model="query.paramType" 
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            >
              <option value="">全部参数</option>
              <option value="WATER_LEVEL">水位监测</option>
              <option value="RAINFALL">雨量监测</option>
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

        <!-- 阈值表格 -->
        <div class="overflow-hidden rounded-lg border border-gray-200">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">监测点</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">参数类型</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警级别</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">阈值</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">持续时间</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="t in list" :key="t.id" class="hover:bg-gray-50 transition-colors">
                <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-900">{{ t.id }}</td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <div class="flex items-center gap-2">
                    <div class="w-2 h-2 bg-blue-400 rounded-full"></div>
                    <span class="text-sm font-medium text-gray-900">{{ pointName(t.pointId) }}</span>
                  </div>
                </td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="getParamTypeClass(t.paramType)">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" v-if="t.paramType === 'WATER_LEVEL'">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"></path>
                    </svg>
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" v-else>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3"></path>
                    </svg>
                    {{ getParamTypeLabel(t.paramType) }}
                  </span>
                </td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="getLevelClass(t.level)">
                    {{ getLevelLabel(t.level) }}
                  </span>
                </td>
                <td class="px-4 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ t.thresholdValue }}</div>
                  <div class="text-xs text-gray-500">{{ getUnit(t.paramType) }}</div>
                </td>
                <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ t.duration }} 分钟
                </td>
                <td class="px-4 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center gap-2">
                    <button 
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-blue-600 bg-blue-100 hover:bg-blue-200 rounded-md transition-colors" 
                      @click="openEdit(t)"
                    >
                      <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                      </svg>
                      编辑
                    </button>
                    <button 
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-red-600 bg-red-100 hover:bg-red-200 rounded-md transition-colors" 
                      @click="remove(t)"
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
      <div class="bg-white rounded-lg shadow-2xl w-full max-w-xl max-h-[90vh] overflow-y-auto">
        <!-- 弹窗头部 -->
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center gap-3">
            <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
              <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900">{{ form.id ? '编辑配置' : '新增配置' }}</h3>
          </div>
        </div>
        
        <!-- 弹窗内容 -->
        <div class="px-6 py-4">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">监测点</label>
              <select 
                v-model="form.pointId" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
              >
                <option value="">请选择监测点</option>
                <option v-for="p in points" :key="p.id" :value="p.id">{{ p.name }}</option>
              </select>
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">参数类型</label>
                <select 
                  v-model="form.paramType" 
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                >
                  <option value="WATER_LEVEL">水位监测</option>
                  <option value="RAINFALL">雨量监测</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">告警级别</label>
                <select 
                  v-model.number="form.level" 
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                >
                  <option :value="1">1级 - 轻微</option>
                  <option :value="2">2级 - 一般</option>
                  <option :value="3">3级 - 严重</option>
                  <option :value="4">4级 - 紧急</option>
                </select>
              </div>
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  阈值 <span class="text-xs text-gray-500">({{ getUnit(form.paramType) }})</span>
                </label>
                <input 
                  v-model.number="form.thresholdValue" 
                  type="number" 
                  step="0.01" 
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                  placeholder="0.00"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">持续时间 (分钟)</label>
                <input 
                  v-model.number="form.duration" 
                  type="number" 
                  min="0" 
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                  placeholder="0"
                />
              </div>
            </div>
            
            <!-- 级别说明 -->
            <div class="bg-gray-50 rounded-lg p-4">
              <h4 class="text-sm font-medium text-gray-700 mb-2">告警级别说明</h4>
              <div class="space-y-1 text-xs text-gray-600">
                <div class="flex items-center gap-2">
                  <span class="inline-block w-2 h-2 bg-green-400 rounded-full"></span>
                  <span><strong>1级</strong> - 轻微告警，需要关注</span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="inline-block w-2 h-2 bg-yellow-400 rounded-full"></span>
                  <span><strong>2级</strong> - 一般告警，需要处理</span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="inline-block w-2 h-2 bg-orange-400 rounded-full"></span>
                  <span><strong>3级</strong> - 严重告警，紧急处理</span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="inline-block w-2 h-2 bg-red-400 rounded-full"></span>
                  <span><strong>4级</strong> - 紧急告警，立即处理</span>
                </div>
              </div>
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
const points = ref([])
const show = ref(false)
const query = reactive({ pointId: '', paramType: '' })
const form = reactive({ id: null, pointId: '', paramType: 'WATER_LEVEL', level: 1, thresholdValue: 0, duration: 0 })

// 获取监测点名称
const pointName = (id) => points.value.find(p => p.id === id)?.name || ('#' + id)

// 获取参数类型样式
const getParamTypeClass = (type) => {
  const typeMap = {
    'WATER_LEVEL': 'bg-blue-100 text-blue-800',
    'RAINFALL': 'bg-indigo-100 text-indigo-800'
  }
  return typeMap[type] || 'bg-gray-100 text-gray-800'
}

// 获取参数类型标签
const getParamTypeLabel = (type) => {
  const labelMap = {
    'WATER_LEVEL': '水位监测',
    'RAINFALL': '雨量监测'
  }
  return labelMap[type] || type
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

// 获取级别标签
const getLevelLabel = (level) => {
  const labelMap = {
    1: '1级 轻微',
    2: '2级 一般',
    3: '3级 严重', 
    4: '4级 紧急'
  }
  return labelMap[level] || `${level}级`
}

// 获取单位
const getUnit = (paramType) => {
  const unitMap = {
    'WATER_LEVEL': 'm',
    'RAINFALL': 'mm'
  }
  return unitMap[paramType] || ''
}

const load = async () => {
  try {
    // 加载监测点数据
    const { data: pd } = await request.get('/api/points')
    if (pd.code === 0) {
      // 按id从小到大排序
      points.value = (pd.data || []).sort((a, b) => a.id - b.id)
    }
    
    // 加载阈值配置数据
    const { data } = await request.get('/api/thresholds', { params: {
        pointId: query.pointId || undefined,
        paramType: query.paramType || undefined
      }})
    if (data.code === 0) {
      // 按id从小到大排序
      list.value = (data.data || []).sort((a, b) => a.id - b.id)
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const openAdd = () => { 
  Object.assign(form, { 
    id: null, 
    pointId: points.value[0]?.id || '', 
    paramType: 'WATER_LEVEL', 
    level: 1, 
    thresholdValue: 0, 
    duration: 0 
  })
  show.value = true 
}

const openEdit = (t) => { Object.assign(form, t); show.value = true }

const save = async () => {
  if (!form.pointId) { 
    alert('请选择监测点')
    return 
  }
  
  try {
    if (form.id) {
      const { data } = await request.put('/api/thresholds/' + form.id, form)
      if (data.code === 0) { show.value = false; load() }
    } else {
      const { data } = await request.post('/api/thresholds', form)
      if (data.code === 0) { show.value = false; load() }
    }
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败，请重试')
  }
}

const remove = async (t) => {
  if (!confirm('确认删除该阈值配置？')) return
  try {
    const { data } = await request.delete('/api/thresholds/' + t.id)
    if (data.code === 0) load()
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败，请重试')
  }
}

onMounted(load)
</script>
