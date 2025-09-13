<template>
  <div class="bg-white p-4 rounded-lg shadow space-y-4">
    <h2 class="text-lg font-semibold">历史查询与趋势图</h2>

    <!-- 查询条件 -->
    <div class="grid md:grid-cols-4 gap-4">
      <div>
        <label class="block text-sm mb-1">监测点</label>
        <select v-model="pointId" class="w-full border rounded px-3 py-2">
          <option disabled value="">请选择监测点</option>
          <option v-for="p in points" :key="p.id" :value="p.id">{{ p.name }}（{{ p.type || '无类型' }}）</option>
        </select>
      </div>

      <div>
        <label class="block text-sm mb-1">开始时间</label>
        <input type="datetime-local" v-model="startLocal" class="w-full border rounded px-3 py-2"/>
      </div>

      <div>
        <label class="block text-sm mb-1">结束时间</label>
        <input type="datetime-local" v-model="endLocal" class="w-full border rounded px-3 py-2"/>
      </div>

      <div class="flex items-end gap-2">
        <button class="px-3 py-2 bg-gray-100 border rounded" @click="setQuickRange(6)">近6小时</button>
        <button class="px-3 py-2 bg-gray-100 border rounded" @click="setQuickRange(24)">近24小时</button>
        <button class="px-3 py-2 bg-gray-100 border rounded" @click="setQuickRange(72)">近72小时</button>
      </div>
    </div>

    <!-- 选择曲线 -->
    <div class="flex items-center gap-4">
      <label class="inline-flex items-center gap-2">
        <input type="checkbox" v-model="show.waterLevel">
        <span>水位（m）</span>
      </label>
      <label class="inline-flex items-center gap-2">
        <input type="checkbox" v-model="show.rainfall">
        <span>雨量（mm）</span>
      </label>
      <label class="inline-flex items-center gap-2">
        <input type="checkbox" v-model="show.flow">
        <span>流量（m³/s）</span>
      </label>
      <button class="ml-auto px-4 py-2 bg-blue-600 text-white rounded" @click="query">查询</button>
    </div>

    <!-- 图表 -->
    <div ref="chartRef" class="w-full" style="height: 420px;"></div>

    <!-- 简要统计 -->
    <div v-if="stats && data.length" class="text-sm text-gray-600">
      <span class="mr-4">样本数：{{ data.length }}</span>
      <span v-if="stats.waterLevel">水位 max/min：{{ stats.waterLevel.max }} / {{ stats.waterLevel.min }}</span>
      <span v-if="stats.rainfall" class="ml-4">雨量总和：{{ stats.rainfall.sum }}</span>
      <span v-if="stats.flow" class="ml-4">流量 max/min：{{ stats.flow.max }} / {{ stats.flow.min }}</span>
    </div>

    <!-- 数据预览（前100行） -->
    <details class="mt-2">
      <summary class="cursor-pointer text-sm text-gray-700">展开查看原始数据（前100）</summary>
      <div class="overflow-auto border rounded mt-2">
        <table class="min-w-full text-sm">
          <thead class="bg-gray-100">
          <tr>
            <th class="px-3 py-2 border">时间</th>
            <th class="px-3 py-2 border">水位</th>
            <th class="px-3 py-2 border">雨量</th>
            <th class="px-3 py-2 border">流量</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(row, i) in data.slice(0,100)" :key="i">
            <td class="px-3 py-2 border whitespace-nowrap">{{ row.timestamp }}</td>
            <td class="px-3 py-2 border">{{ row.waterLevel ?? '' }}</td>
            <td class="px-3 py-2 border">{{ row.rainfall ?? '' }}</td>
            <td class="px-3 py-2 border">{{ row.flow ?? '' }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </details>
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
  if (resp.code === 0) points.value = resp.data || []
}

function setQuickRange(hours) {
  const end = new Date()
  const start = new Date(end.getTime() - hours*3600*1000)
  startLocal.value = toLocalInput(start)
  endLocal.value = toLocalInput(end)
}

async function query() {
  if (!pointId.value) { alert('请选择监测点'); return }
  const start = fromLocalInput(startLocal.value) // Date
  const end = fromLocalInput(endLocal.value)
  if (!start || !end || start >= end) { alert('时间范围不正确'); return }

  const { data: resp } = await request.get('/api/monitor-data', {
    params: {
      pointId: pointId.value,
      start: fmtBackend(start),
      end: fmtBackend(end),
    }
  })
  if (resp.code !== 0) { alert(resp.msg || '查询失败'); return }

  data.value = resp.data || []
  // 自动抽样，避免一次渲染过多点
  const simplified = decimate(data.value, 5000)

  // 计算简单统计
  stats.value = computeStats(data.value)

  renderChart(simplified)
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
    data: wl
  })
  if (hasFL) series.push({
    name: '流量(m³/s)',
    type: 'line',
    yAxisIndex: 0,
    showSymbol: false,
    smooth: true,
    data: fl
  })
  if (hasRF) series.push({
    name: '雨量(mm)',
    type: 'line',
    yAxisIndex: 1,
    showSymbol: false,
    smooth: true,
    data: rf
  })

  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
    legend: { top: 8 },
    grid: { left: 60, right: 60, top: 40, bottom: 80 },
    dataZoom: [
      { type: 'inside' },
      { type: 'slider', height: 24, bottom: 24 }
    ],
    xAxis: {
      type: 'time',
      axisLabel: { formatter: (v) => fmtTick(new Date(v)) }
    },
    yAxis: [
      {
        type: 'value',
        name: '水位/流量',
        position: 'left',
        scale: true,
        splitLine: { show: true }
      },
      {
        type: 'value',
        name: '雨量',
        position: 'right',
        scale: true,
        splitLine: { show: false }
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
  const res = {}
  const col = (k) => rows.map(r => r[k]).filter(v => v!=null)
  const max = (arr) => arr.length ? Math.max(...arr) : null
  const min = (arr) => arr.length ? Math.min(...arr) : null
  const sum = (arr) => arr.reduce((a,b)=>a+b,0)

  const wl = col('waterLevel')
  const rf = col('rainfall')
  const fl = col('flow')

  if (wl.length) res.waterLevel = { max: round(max(wl)), min: round(min(wl)) }
  if (rf.length) res.rainfall   = { sum: round(sum(rf)) }
  if (fl.length) res.flow       = { max: round(max(fl)), min: round(min(fl)) }
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
>