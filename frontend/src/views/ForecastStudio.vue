<template>
  <div class="bg-white p-4 rounded-lg shadow space-y-4">
    <!-- 标题 + 状态 -->
    <div class="flex items-center justify-between">
      <h2 class="text-lg font-semibold">预测工作台</h2>
      <div class="text-xs" :class="status.ok ? 'text-green-600' : 'text-gray-500'">
        {{ status.text || '选择监测点与时间窗，加载历史与未来数据并生成预测结果。' }}
      </div>
    </div>

    <!-- 条件区（已精简：去掉模型、噪声、步长等） -->
    <div class="grid md:grid-cols-4 gap-4">
      <div>
        <label class="block text-sm mb-1">监测点</label>
        <select v-model="pointId" class="w-full border rounded px-3 py-2">
          <option disabled value="">请选择监测点</option>
          <option v-for="p in points" :key="p.id" :value="p.id">
            {{ p.name }}（{{ p.type || '无类型' }}）
          </option>
        </select>
      </div>

      <div>
        <label class="block text-sm mb-1">预测起点（含）</label>
        <input type="datetime-local" v-model="forecastStartLocal" class="w-full border rounded px-3 py-2"/>
        <p class="text-xs text-gray-500 mt-1">起点之前为历史，之后即为“预测”（直接显示未来数据）。</p>
      </div>

      <div>
        <label class="block text-sm mb-1">预测时长（小时）</label>
        <input type="number" v-model.number="horizonHours" min="1" max="168" class="w-full border rounded px-3 py-2"/>
      </div>

      <div class="col-span-1"></div>
    </div>

    <div class="grid md:grid-cols-4 gap-4">
      <div class="col-span-2">
        <label class="block text-sm mb-1">数据时间窗</label>
        <div class="flex gap-2">
          <input type="datetime-local" v-model="windowStartLocal" class="w-full border rounded px-3 py-2"/>
          <span class="px-1 py-2 text-gray-400">至</span>
          <input type="datetime-local" v-model="windowEndLocal" class="w-full border rounded px-3 py-2"/>
        </div>
        <p class="text-xs text-gray-500 mt-1">建议覆盖“预测起点前48h ~ 预测起点后{{ horizonHours }}h”。</p>
      </div>

      <div>
        <label class="block text-sm mb-1">显示变量</label>
        <div class="flex items-center gap-4">
          <label class="inline-flex items-center gap-2"><input type="checkbox" v-model="show.waterLevel"> <span>水位(m)</span></label>
          <label class="inline-flex items-center gap-2"><input type="checkbox" v-model="show.rainfall"> <span>雨量(mm)</span></label>
          <label class="inline-flex items-center gap-2"><input type="checkbox" v-model="show.flow"> <span>流量(m³/s)</span></label>
        </div>
      </div>

      <div class="flex items-end">
        <div class="ml-auto flex gap-2">
          <button class="px-3 py-2 bg-gray-100 border rounded" @click="quickRange(48)">历史48h + 未来{{ horizonHours }}h</button>
          <button class="px-3 py-2 bg-blue-600 text-white rounded" @click="runPredict" :disabled="status.loading">
            {{ status.loading ? '加载中…' : '加载数据并生成预测' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 图表 -->
    <div ref="chartRef" class="w-full" style="height: 460px;"></div>

    <!-- 简要指标（基于“未来段”本身） -->
    <div class="grid md:grid-cols-3 gap-4">
      <div class="border rounded p-3">
        <div class="text-sm text-gray-500">水位(m)</div>
        <div class="mt-2 text-sm">
          <div>未来峰值：{{ metrics.waterLevel.max ?? '-' }}</div>
        </div>
      </div>
      <div class="border rounded p-3">
        <div class="text-sm text-gray-500">雨量(mm)</div>
        <div class="mt-2 text-sm">
          <div>未来累计雨量：{{ metrics.rainfall.sum ?? '-' }}</div>
        </div>
      </div>
      <div class="border rounded p-3">
        <div class="text-sm text-gray-500">流量(m³/s)</div>
        <div class="mt-2 text-sm">
          <div>未来峰值：{{ metrics.flow.max ?? '-' }}</div>
        </div>
      </div>
    </div>

    <!-- 预测明细（= 未来段明细） -->
    <div class="border rounded">
      <div class="px-3 py-2 bg-gray-50 font-medium">预测明细</div>
      <div class="overflow-auto">
        <table class="min-w-full text-sm">
          <thead class="bg-gray-100">
          <tr>
            <th class="px-3 py-2 border whitespace-nowrap">时间</th>
            <th class="px-3 py-2 border" v-if="show.waterLevel">水位</th>
            <th class="px-3 py-2 border" v-if="show.rainfall">雨量</th>
            <th class="px-3 py-2 border" v-if="show.flow">流量</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(row,i) in tableRows" :key="i">
            <td class="px-3 py-2 border whitespace-nowrap">{{ row.time }}</td>
            <td class="px-3 py-2 border" v-if="show.waterLevel">{{ row.wl ?? '' }}</td>
            <td class="px-3 py-2 border" v-if="show.rainfall">{{ row.rf ?? '' }}</td>
            <td class="px-3 py-2 border" v-if="show.flow">{{ row.fl ?? '' }}</td>
          </tr>
          <tr v-if="!tableRows.length">
            <td :colspan="1 + (show.waterLevel?1:0) + (show.rainfall?1:0) + (show.flow?1:0)"
                class="px-3 py-6 text-center text-gray-500">
              未来时间段无数据，请调整时间窗或确认数据库导入。
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 文本总结 -->
    <div class="border rounded p-3 text-sm leading-6">
      <div class="font-medium mb-1">预测总结</div>
      <p v-if="summaryText">{{ summaryText }}</p>
      <p v-else class="text-gray-500">点击“加载数据并生成预测”后自动生成。</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import request from '../utils/request'

/** ====== 状态 ====== */
const status = reactive({ loading: false, ok: false, text: '' })

const points = ref([])
const pointId = ref('')

const forecastStartLocal = ref(toLocalInput(roundUpToHour(new Date())))
const horizonHours = ref(24)

const windowStartLocal = ref(toLocalInput(new Date(Date.now() - 48*3600*1000)))
const windowEndLocal   = ref(toLocalInput(new Date(Date.now() + 24*3600*1000)))

const show = reactive({ waterLevel: true, rainfall: true, flow: false })

const chartRef = ref(null)
let chart = null

// 数据容器
let rows = []                 // 全部数据（历史+未来）
let hist = { wl: [], rf: [], fl: [] }   // 历史（<= 起点）
let future = { wl: [], rf: [], fl: [] } // 未来（> 起点）= 预测

// 指标与表格
const metrics = reactive({
  waterLevel: { max: null },
  rainfall:   { sum: null },
  flow:       { max: null },
})
const tableRows = ref([])
const summaryText = ref('')

/** ====== 生命周期 ====== */
onMounted(async () => {
  await loadPoints()
  if (!pointId.value && points.value.length) pointId.value = points.value[0].id
  await runPredict()
  window.addEventListener('resize', resize)
})
onBeforeUnmount(() => {
  if (chart) chart.dispose()
  window.removeEventListener('resize', resize)
})

/** ====== 主流程 ====== */
async function runPredict() {
  if (!pointId.value) { alert('请选择监测点'); return }
  status.loading = true
  status.ok = false
  status.text = '加载中…'
  try {
    await loadMonitorData()

    status.text = rows.length
        ? `已加载 ${rows.length} 条记录`
        : '接口未返回数据，请检查接口或调整时间窗。'

    splitPastFuture()
    buildMetrics()
    buildTable()
    renderChart()
    buildSummary()

    status.ok = true
  } catch (err) {
    console.error('[forecast] runPredict error:', err)
    status.ok = false
    status.text = '渲染失败：' + (err?.message || err)
  } finally {
    // 不管成功还是失败，都关掉“加载中…”
    status.loading = false
  }
}

/** 1) 拉取监测数据（精简：只请求 /api/monitor-data；兼容 {code,data} 或数组） */
async function loadMonitorData() {
  rows = []
  const start = fromLocalInput(windowStartLocal.value)
  // 末尾 +59s，包含整分边界
  const end   = new Date(fromLocalInput(windowEndLocal.value).getTime() + 59*1000)

  try {
    const { data: resp } = await request.get('/api/monitor-data', {
      params: { pointId: pointId.value, start: fmtBackend(start), end: fmtBackend(end) }
    })
    const arr = normalizeResponse(resp)
    rows = (arr || [])
        .map(r => ({
          t: toMs(r.timestamp || r.time || r.ts || r.dateTime || r.datetime),
          wl: toNum(r.waterLevel ?? r.water_level ?? r.wl),
          rf: toNum(r.rainfall   ?? r.rain      ?? r.rf),
          fl: toNum(r.flow       ?? r.discharge ?? r.q),
        }))
        .filter(r => r.t != null)
        .sort((a,b)=>a.t-b.t)
  } catch (e) {
    rows = []
  }
}

function normalizeResponse(payload){
  if (!payload) return null
  if (Array.isArray(payload)) return payload
  if (Array.isArray(payload.data)) return payload.data
  return null
}

/** 2) 按“预测起点”拆分（<= 起点为历史，> 起点为未来=预测） */
function splitPastFuture() {
  const cutMs = fromLocalInput(forecastStartLocal.value).getTime()
  hist   = { wl: [], rf: [], fl: [] }
  future = { wl: [], rf: [], fl: [] }
  for (const r of rows) {
    const bucket = (r.t <= cutMs) ? hist : future
    if (r.wl != null) bucket.wl.push([r.t, r.wl])
    if (r.rf != null) bucket.rf.push([r.t, r.rf])
    if (r.fl != null) bucket.fl.push([r.t, r.fl])
  }
}

/** 3) 指标：直接基于“未来段” */
function buildMetrics() {
  metrics.waterLevel.max = fmtNum(maxValue(future.wl))
  metrics.rainfall.sum   = fmtNum(sumValue(future.rf))
  metrics.flow.max       = fmtNum(maxValue(future.fl))
}

/** 4) 明细表：直接列出未来段 */
function buildTable() {
  const unionTimes = unionSorted([future.wl, future.rf, future.fl].map(s => s.map(x=>x[0])))
  const fMap = { wl: toMap(future.wl), rf: toMap(future.rf), fl: toMap(future.fl) }
  tableRows.value = unionTimes.map(t => ({
    time: fmtTick(new Date(t)),
    wl: fmtNum(fMap.wl.get(t)),
    rf: fmtNum(fMap.rf.get(t)),
    fl: fmtNum(fMap.fl.get(t)),
  }))
}

/** 5) 图表：历史（实线） + 预测=未来（虚线） */
function renderChart() {
  if (!chart) chart = echarts.init(chartRef.value)
  const series = []
  if (show.waterLevel && hist.wl.length) series.push(line('历史-水位', hist.wl))
  if (show.flow       && hist.fl.length) series.push(line('历史-流量', hist.fl))
  if (show.rainfall   && hist.rf.length) series.push(line('历史-雨量', hist.rf, 1))

  if (show.waterLevel && future.wl.length) series.push(lineDash('预测-水位', future.wl))
  if (show.flow       && future.fl.length) series.push(lineDash('预测-流量', future.fl))
  if (show.rainfall   && future.rf.length) series.push(lineDash('预测-雨量', future.rf, 1))

  const startX = futureTimeStart()
  const endX   = futureTimeEnd()

  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
    legend: { top: 8 },
    grid: { left: 60, right: 60, top: 40, bottom: 60 },
    dataZoom: [{ type: 'inside' }, { type: 'slider', height: 24, bottom: 24 }],
    xAxis: { type: 'time' },
    yAxis: [
      { type: 'value', name: '水位/流量', position: 'left',  scale: true, splitLine: { show: true } },
      { type: 'value', name: '雨量',     position: 'right', scale: true, splitLine: { show: false } }
    ],
    series
  }
  if (startX && endX) {
    option.series.push({
      name: '预测区', type: 'line', data: [],
      markArea: { itemStyle: { color: 'rgba(149,128,255,.10)' },
        data: [[{ xAxis: new Date(startX) }, { xAxis: new Date(endX) }]] }
    })
  }
  chart.setOption(option, true)
  resize()
}

/** 6) 文本总结：基于未来段的统计 */
function buildSummary() {
  const wlMax = metrics.waterLevel.max
  const rfSum = metrics.rainfall.sum
  const flMax = metrics.flow.max

  const p = []
  p.push(`基于监测点「${pointName(pointId.value)}」的数据，已生成预测（直接使用“预测起点”之后的未来数据）。窗口：${fmtTick(fromLocalInput(forecastStartLocal.value))} 起后 ${horizonHours.value} 小时。`)
  if (wlMax != null) p.push(`未来水位峰值约 ${wlMax} m。`)
  if (rfSum != null) p.push(`未来累计雨量约 ${rfSum} mm。`)
  if (flMax != null) p.push(`未来流量峰值约 ${flMax} m³/s。`)
  summaryText.value = p.join(' ')
}

/** ====== 工具 ====== */
function toLocalInput(d) {
  const y=d.getFullYear(), m=String(d.getMonth()+1).padStart(2,'0'), day=String(d.getDate()).padStart(2,'0')
  const hh=String(d.getHours()).padStart(2,'0'), mm=String(d.getMinutes()).padStart(2,'0')
  return `${y}-${m}-${day}T${hh}:${mm}`
}
function fromLocalInput(s) { return s?.includes('T') ? new Date(s) : new Date(String(s || '').replace(' ','T')) }
function roundUpToHour(d) { const nd=new Date(d); nd.setMinutes(0,0,0); nd.setHours(nd.getHours()+1); return nd }
function fmtBackend(d) {
  const y=d.getFullYear(), m=String(d.getMonth()+1).padStart(2,'0'), day=String(d.getDate()).padStart(2,'0')
  const hh=String(d.getHours()).padStart(2,'0'), mi=String(d.getMinutes()).padStart(2,'0'), ss=String(d.getSeconds()).padStart(2,'0')
  return `${y}-${m}-${day} ${hh}:${mi}:${ss}`
}
function strToDate(s) { return new Date(String(s).replace(' ', 'T')) }
function fmtTick(d) {
  const m=String(d.getMonth()+1).padStart(2,'0'), day=String(d.getDate()).padStart(2,'0')
  const hh=String(d.getHours()).padStart(2,'0'), mi=String(d.getMinutes()).padStart(2,'0')
  return `${m}-${day} ${hh}:${mi}`
}
function resize(){ if (chart) chart.resize() }
function toNum(v){ const n=Number(v); return Number.isFinite(n)? n : null }
function toMs(x){ if(!x) return null; return strToDate(String(x)).getTime() }
function maxValue(arr){ if(!arr?.length) return null; return round2(Math.max(...arr.map(x=>x[1]))) }
function sumValue(arr){ if(!arr?.length) return null; return round2(arr.reduce((a,b)=>a+(b[1]||0),0)) }
function round2(v){ return v==null? null : Math.round(v*100)/100 }
function pointName(id){ const p=points.value.find(x=>x.id===id); return p? (p.name || ('#'+id)) : ('#'+id) }
function unionSorted(listOfTs){ const set=new Set(); for(const ts of listOfTs) for(const t of ts) set.add(t); return [...set].sort((a,b)=>a-b) }
function toMap(arr){ const m=new Map(); for(const [t,v] of arr) m.set(t,v); return m }
function futureTimeStart(){ const arr=[future.wl,future.rf,future.fl].find(s=>s&&s.length); return arr?arr[0][0]:null }
function futureTimeEnd(){   const arr=[future.wl,future.rf,future.fl].find(s=>s&&s.length); return arr?arr.at(-1)[0]:null }

// 数值格式化（保留两位小数；null/NaN 输出 null，方便表格和图表判断）
function fmtNum(v) {
  if (v == null) return null
  const n = Number(v)
  if (!Number.isFinite(n)) return null
  return Math.round(n * 100) / 100
}


/** 监测点列表（与项目其它页一致） */
async function loadPoints() {
  try {
    const { data } = await request.get('/api/points')
    if (data && Array.isArray(data.data)) points.value = data.data
    else if (Array.isArray(data)) points.value = data
    else throw new Error('bad format')
  } catch {
    points.value = []
  }
}

/** 快捷窗口：历史48h + 未来horizonHours */
function quickRange(histHours=48){
  const start = new Date(fromLocalInput(forecastStartLocal.value).getTime() - histHours*3600*1000)
  const end   = new Date(fromLocalInput(forecastStartLocal.value).getTime() + horizonHours.value*3600*1000)
  windowStartLocal.value = toLocalInput(start)
  windowEndLocal.value   = toLocalInput(end)
}

/** 折线渲染辅助 */
function line(name, data, yAxisIndex=0) {
  return { name, type:'line', yAxisIndex, showSymbol:false, smooth:true, data }
}
function lineDash(name, data, yAxisIndex=0) {
  return { name, type:'line', yAxisIndex, showSymbol:false, smooth:true, data,
    lineStyle:{ type:'dashed', width:2 } }
}
</script>
