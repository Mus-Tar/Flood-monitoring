<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 px-4 py-6">
    <div class="max-w-7xl mx-auto space-y-6">
      <!-- 页面标题 -->
      <div class="mb-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
            </svg>
          </div>
          <h1 class="text-2xl font-bold text-gray-900">历史数据分析</h1>
        </div>
        <p class="text-gray-600">查看监测点的历史数据趋势图，分析变化规律</p>
      </div>

      <!-- 查询条件卡片 -->
      <div class="bg-white rounded-lg shadow-xl p-6">
        <div class="flex items-center gap-2 mb-4">
          <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
          </svg>
          <h2 class="text-lg font-semibold text-gray-900">查询条件</h2>
        </div>
        
        <div class="grid md:grid-cols-4 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">监测点</label>
            <select 
              v-model="pointId" 
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            >
              <option disabled value="">请选择监测点</option>
              <option v-for="p in points" :key="p.id" :value="p.id">
                {{ p.name }} ({{ p.type || '无类型' }})
              </option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">开始时间</label>
            <input 
              type="datetime-local" 
              v-model="startLocal" 
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">结束时间</label>
            <input 
              type="datetime-local" 
              v-model="endLocal" 
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            />
          </div>

          <div class="flex flex-col">
            <label class="block text-sm font-medium text-gray-700 mb-2">快速选择</label>
            <div class="flex gap-2">
              <button 
                class="flex-1 px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 border border-gray-300 rounded-lg transition-colors" 
                @click="setQuickRange(6)"
              >
                6小时
              </button>
              <button 
                class="flex-1 px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 border border-gray-300 rounded-lg transition-colors" 
                @click="setQuickRange(24)"
              >
                24小时
              </button>
              <button 
                class="flex-1 px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 border border-gray-300 rounded-lg transition-colors" 
                @click="setQuickRange(72)"
              >
                3天
              </button>
            </div>
          </div>
        </div>

        <!-- 图表选项 -->
        <div class="mt-6 pt-4 border-t border-gray-200">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-6">
              <span class="text-sm font-medium text-gray-700">显示数据:</span>
              
              <label class="inline-flex items-center gap-2 cursor-pointer">
                <input 
                  type="checkbox" 
                  v-model="show.waterLevel"
                  class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                />
                <span class="flex items-center gap-2 text-sm text-gray-700">
                  <div class="w-3 h-3 bg-blue-500 rounded-full"></div>
                  水位（m）
                </span>
              </label>
              
              <label class="inline-flex items-center gap-2 cursor-pointer">
                <input 
                  type="checkbox" 
                  v-model="show.rainfall"
                  class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                />
                <span class="flex items-center gap-2 text-sm text-gray-700">
                  <div class="w-3 h-3 bg-green-500 rounded-full"></div>
                  雨量（mm）
                </span>
              </label>
              
              <label class="inline-flex items-center gap-2 cursor-pointer">
                <input 
                  type="checkbox" 
                  v-model="show.flow"
                  class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                />
                <span class="flex items-center gap-2 text-sm text-gray-700">
                  <div class="w-3 h-3 bg-purple-500 rounded-full"></div>
                  流量（m³/s）
                </span>
              </label>
            </div>
            
            <button 
              class="px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors flex items-center gap-2 font-medium" 
              @click="query"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
              </svg>
              查询分析
            </button>
          </div>
        </div>
      </div>

      <!-- 图表展示卡片 -->
      <div class="bg-white rounded-lg shadow-xl p-6">
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center gap-2">
            <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 12l3-3 3 3 4-4"></path>
            </svg>
            <h2 class="text-lg font-semibold text-gray-900">趋势分析图</h2>
          </div>
          <div v-if="data.length" class="text-sm text-gray-500">
            数据点数: {{ data.length }}
          </div>
        </div>
        
        <!-- 图表容器 -->
        <div class="bg-gray-50 rounded-lg p-4" style="min-height: 420px;">
          <div ref="chartRef" class="w-full" style="height: 420px;"></div>
        </div>
      </div>

      <!-- 统计信息卡片 -->
      <div v-if="stats && data.length" class="bg-white rounded-lg shadow-xl p-6">
        <div class="flex items-center gap-2 mb-4">
          <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V9a2 2 0 012-2h10a2 2 0 012 2v8a2 2 0 01-2 2z"></path>
          </svg>
          <h2 class="text-lg font-semibold text-gray-900">数据统计</h2>
        </div>
        
        <div class="grid md:grid-cols-3 gap-6">
          <div v-if="stats.waterLevel" class="bg-blue-50 rounded-lg p-4">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"></path>
                </svg>
              </div>
              <h3 class="font-medium text-gray-900">水位统计</h3>
            </div>
            <div class="space-y-2 text-sm">
              <div class="flex justify-between">
                <span class="text-gray-600">最高值:</span>
                <span class="font-medium text-blue-700">{{ stats.waterLevel.max }} m</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">最低值:</span>
                <span class="font-medium text-blue-700">{{ stats.waterLevel.min }} m</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">平均值:</span>
                <span class="font-medium text-blue-700">{{ stats.waterLevel.avg }} m</span>
              </div>
            </div>
          </div>
          
          <div v-if="stats.rainfall" class="bg-green-50 rounded-lg p-4">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
                <svg class="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3"></path>
                </svg>
              </div>
              <h3 class="font-medium text-gray-900">雨量统计</h3>
            </div>
            <div class="space-y-2 text-sm">
              <div class="flex justify-between">
                <span class="text-gray-600">累计雨量:</span>
                <span class="font-medium text-green-700">{{ stats.rainfall.sum }} mm</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">平均雨量:</span>
                <span class="font-medium text-green-700">{{ stats.rainfall.avg }} mm</span>
              </div>
            </div>
          </div>
          
          <div v-if="stats.flow" class="bg-purple-50 rounded-lg p-4">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-8 h-8 bg-purple-100 rounded-full flex items-center justify-center">
                <svg class="w-4 h-4 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
                </svg>
              </div>
              <h3 class="font-medium text-gray-900">流量统计</h3>
            </div>
            <div class="space-y-2 text-sm">
              <div class="flex justify-between">
                <span class="text-gray-600">最大流量:</span>
                <span class="font-medium text-purple-700">{{ stats.flow.max }} m³/s</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">最小流量:</span>
                <span class="font-medium text-purple-700">{{ stats.flow.min }} m³/s</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">平均流量:</span>
                <span class="font-medium text-purple-700">{{ stats.flow.avg }} m³/s</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 数据预览 -->
      <div class="bg-white rounded-lg shadow-xl">
        <div class="px-6 py-4 border-b border-gray-200">
          <details class="group">
            <summary class="cursor-pointer flex items-center gap-2 text-lg font-semibold text-gray-900 hover:text-blue-600">
              <svg class="w-4 h-4 transition-transform group-open:rotate-90" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
              </svg>
              原始数据预览 (前100条)
            </summary>
            
            <div class="mt-4">
              <div class="overflow-auto border border-gray-200 rounded-lg">
                <table class="min-w-full text-sm divide-y divide-gray-200">
                  <thead class="bg-gray-50">
                    <tr>
                      <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">时间</th>
                      <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">水位 (m)</th>
                      <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">雨量 (mm)</th>
                      <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">流量 (m³/s)</th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="(row, i) in data.slice(0, 100)" :key="i" class="hover:bg-gray-50">
                      <td class="px-4 py-3 whitespace-nowrap text-gray-900 font-mono">{{ row.timestamp }}</td>
                      <td class="px-4 py-3 text-center">{{ row.waterLevel ?? '-' }}</td>
                      <td class="px-4 py-3 text-center">{{ row.rainfall ?? '-' }}</td>
                      <td class="px-4 py-3 text-center">{{ row.flow ?? '-' }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </details>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import request from '../utils/request'

/** --- 状态 --- */
const points = ref([])
const pointId = ref('')
const chartRef = ref(null)
let chart = null

// 勾选显示的序列
const show = reactive({
  waterLevel: true,
  rainfall: true,
  flow: false,
})

/** 时间范围：默认最近24小时 */
const now = new Date()
const endLocal = ref(toLocalInput(now))
const startLocal = ref(toLocalInput(new Date(now.getTime() - 24*3600*1000)))

/** 原始数据（后端返回） */
const data = ref([])
/** 统计信息（简单示例） */
const stats = ref(null)

/** --- 生命周期 --- */
onMounted(async () => {
  await loadPoints()
  if (!pointId.value && points.value.length) pointId.value = points.value[0].id
  await query()
  window.addEventListener('resize', resize)
})
onBeforeUnmount(() => {
  if (chart) chart.dispose()
  window.removeEventListener('resize', resize)
})

/** --- 方法区 --- */
async function loadPoints() {
  const { data: resp } = await request.get('/api/points')
  if (resp.code === 0) {
    // 按id从小到大排序
    points.value = (resp.data || []).sort((a, b) => a.id - b.id)
  }
}

function setQuickRange(hours) {
  const end = new Date()
  const start = new Date(end.getTime() - hours*3600*1000)
  startLocal.value = toLocalInput(start)
  endLocal.value = toLocalInput(end)
}

async function query() {
  if (!pointId.value) { 
    alert('请选择监测点')
    return 
  }
  
  const start = fromLocalInput(startLocal.value)
  const end = fromLocalInput(endLocal.value)
  if (!start || !end || start >= end) { 
    alert('时间范围不正确')
    return 
  }

  try {
    const { data: resp } = await request.get('/api/monitor-data', {
      params: {
        pointId: pointId.value,
        start: fmtBackend(start),
        end: fmtBackend(end),
      }
    })
    
    if (resp.code !== 0) { 
      alert(resp.msg || '查询失败')
      return 
    }

    data.value = resp.data || []
    
    // 自动抽样，避免一次渲染过多点
    const simplified = decimate(data.value, 5000)

    // 计算简单统计
    stats.value = computeStats(data.value)

    renderChart(simplified)
  } catch (error) {
    console.error('查询失败:', error)
    alert('查询失败，请检查网络连接')
  }
}

function renderChart(rows) {
  if (!chart) {
    chart = echarts.init(chartRef.value)
  }
  const series = []
  const hasWL = show.waterLevel
  const hasRF = show.rainfall
  const hasFL = show.flow

  const wl = rows.filter(r => r.waterLevel != null).map(r => [strToDate(r.timestamp).getTime(), r.waterLevel])
  const rf = rows.filter(r => r.rainfall != null).map(r => [strToDate(r.timestamp).getTime(), r.rainfall])
  const fl = rows.filter(r => r.flow != null).map(r => [strToDate(r.timestamp).getTime(), r.flow])

  if (hasWL) series.push({
    name: '水位(m)',
    type: 'line',
    yAxisIndex: 0,
    showSymbol: false,
    smooth: true,
    lineStyle: { width: 2, color: '#3B82F6' },
    areaStyle: { color: 'rgba(59, 130, 246, 0.1)' },
    data: wl
  })
  if (hasFL) series.push({
    name: '流量(m³/s)',
    type: 'line',
    yAxisIndex: 0,
    showSymbol: false,
    smooth: true,
    lineStyle: { width: 2, color: '#8B5CF6' },
    data: fl
  })
  if (hasRF) series.push({
    name: '雨量(mm)',
    type: 'bar',
    yAxisIndex: 1,
    itemStyle: { color: '#10B981' },
    barMaxWidth: 10,
    data: rf
  })

  const option = {
    tooltip: { 
      trigger: 'axis', 
      axisPointer: { type: 'cross' },
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e5e7eb',
      borderWidth: 1,
      textStyle: { color: '#374151' }
    },
    legend: { 
      top: 8,
      itemGap: 20,
      textStyle: { color: '#374151' }
    },
    grid: { 
      left: 80, 
      right: 80, 
      top: 50, 
      bottom: 100,
      containLabel: true
    },
    dataZoom: [
      { 
        type: 'inside',
        xAxisIndex: 0,
        filterMode: 'none'
      },
      { 
        type: 'slider', 
        height: 30, 
        bottom: 30,
        backgroundColor: '#f8fafc',
        borderColor: '#e5e7eb',
        fillerColor: 'rgba(59, 130, 246, 0.2)',
        handleStyle: { color: '#3B82F6' },
        textStyle: { color: '#6b7280' }
      }
    ],
    xAxis: {
      type: 'time',
      axisLabel: { 
        formatter: (v) => fmtTick(new Date(v)),
        color: '#6b7280'
      },
      axisLine: { lineStyle: { color: '#e5e7eb' } },
      axisTick: { lineStyle: { color: '#e5e7eb' } }
    },
    yAxis: [
      {
        type: 'value',
        name: '水位/流量',
        nameTextStyle: { color: '#374151' },
        position: 'left',
        scale: true,
        splitLine: { 
          show: true,
          lineStyle: { color: '#f3f4f6', type: 'dashed' }
        },
        axisLabel: { color: '#6b7280' },
        axisLine: { lineStyle: { color: '#e5e7eb' } }
      },
      {
        type: 'value',
        name: '雨量',
        nameTextStyle: { color: '#374151' },
        position: 'right',
        scale: true,
        splitLine: { show: false },
        axisLabel: { color: '#6b7280' },
        axisLine: { lineStyle: { color: '#e5e7eb' } }
      }
    ],
    series
  }

  chart.setOption(option, true)
  resize()
}

function resize() {
  if (chart) chart.resize()
}

/** --- 工具函数 --- */
// "yyyy-MM-ddTHH:mm" 给 input[type=datetime-local]
function toLocalInput(d) {
  const y = d.getFullYear()
  const m = String(d.getMonth()+1).padStart(2,'0')
  const day = String(d.getDate()).padStart(2,'0')
  const hh = String(d.getHours()).padStart(2,'0')
  const mm = String(d.getMinutes()).padStart(2,'0')
  return `${y}-${m}-${day}T${hh}:${mm}`
}
function fromLocalInput(s) {
  if (!s) return null
  // s like "2025-09-10T08:00"
  return new Date(s)
}
// 后端需要 "yyyy-MM-dd HH:mm:ss"
function fmtBackend(d) {
  const y = d.getFullYear()
  const m = String(d.getMonth()+1).padStart(2,'0')
  const day = String(d.getDate()).padStart(2,'0')
  const hh = String(d.getHours()).padStart(2,'0')
  const mi = String(d.getMinutes()).padStart(2,'0')
  const ss = String(d.getSeconds()).padStart(2,'0')
  return `${y}-${m}-${day} ${hh}:${mi}:${ss}`
}
// tick label 简化
function fmtTick(d) {
  const m = String(d.getMonth()+1).padStart(2,'0')
  const day = String(d.getDate()).padStart(2,'0')
  const hh = String(d.getHours()).padStart(2,'0')
  const mi = String(d.getMinutes()).padStart(2,'0')
  return `${m}-${day} ${hh}:${mi}`
}
// 把 "yyyy-MM-dd HH:mm:ss" 转 Date
function strToDate(s) {
  return new Date(s.replace(' ', 'T'))
}
// 统计信息
function computeStats(rows) {
  if (!rows || rows.length === 0) {
    return {
      count: 0,
      waterLevel: { max: 0, min: 0, avg: 0 },
      rainfall: { sum: 0, avg: 0 },
      flow: { max: 0, min: 0, avg: 0 }
    }
  }

  const res = { count: rows.length }
  const col = (k) => rows.map(r => r[k]).filter(v => v != null && !isNaN(v))
  const max = (arr) => arr.length ? Math.max(...arr) : 0
  const min = (arr) => arr.length ? Math.min(...arr) : 0
  const sum = (arr) => arr.reduce((a, b) => a + b, 0)
  const avg = (arr) => arr.length ? sum(arr) / arr.length : 0

  const wl = col('waterLevel')
  const rf = col('rainfall')
  const fl = col('flow')

  res.waterLevel = { 
    max: round(max(wl)), 
    min: round(min(wl)),
    avg: round(avg(wl))
  }
  res.rainfall = { 
    sum: round(sum(rf)),
    avg: round(avg(rf))
  }
  res.flow = { 
    max: round(max(fl)), 
    min: round(min(fl)),
    avg: round(avg(fl))
  }

  return res
}
function round(v) { return v==null ? null : Math.round(v*100)/100 }

// 简单抽稀：超过 maxPoints 时近似均匀取样
function decimate(rows, maxPoints=5000) {
  if (rows.length <= maxPoints) return rows
  const step = Math.ceil(rows.length / maxPoints)
  const out = []
  for (let i=0; i<rows.length; i+=step) out.push(rows[i])
  return out
}
</script>