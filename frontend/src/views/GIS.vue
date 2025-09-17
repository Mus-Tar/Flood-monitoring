<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
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
          <h1 class="text-2xl font-bold text-gray-900">GIS地理信息系统</h1>
        </div>
        <p class="text-gray-600">管理监测点位置信息，可视化展示地理分布</p>
      </div>

      <div class="grid grid-cols-12 gap-6">
        <!-- 侧栏：站点列表与操作 -->
        <div class="col-span-12 lg:col-span-4">
          <div class="bg-white rounded-lg shadow-xl p-6">
            <div class="flex items-center justify-between mb-4">
              <h2 class="text-lg font-semibold text-gray-900">监测点地图</h2>
              <div class="flex gap-2">
                <button class="px-3 py-2 text-sm bg-blue-50 text-blue-600 border border-blue-200 rounded-lg hover:bg-blue-100 transition-colors" @click="reload">刷新</button>
                <button class="px-3 py-2 text-sm border rounded-lg transition-colors"
                        :class="filterOnlyOnMap ? 'bg-green-50 text-green-600 border-green-200' : 'bg-gray-50 text-gray-600 border-gray-200'"
                        @click="toggleFilterOnly">
                  地图仅显示筛选: <b>{{ filterOnlyOnMap ? '是' : '否' }}</b>
                </button>
              </div>
            </div>

            <div class="flex items-center gap-2 mb-4">
              <input
                  v-model="keyword"
                  @keyup.enter="applyFilter"
                  placeholder="按名称/类型/流域筛选"
                  class="border border-gray-300 rounded-lg px-3 py-2 w-full focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
              />
              <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors" @click="applyFilter">搜索</button>
            </div>

            <div class="bg-gray-50 rounded-lg border border-gray-200 overflow-hidden">
              <div class="max-h-[520px] overflow-auto">
                <table class="min-w-full text-sm">
                  <thead class="bg-gray-100 sticky top-0">
                    <tr>
                      <th class="px-3 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">#</th>
                      <th class="px-3 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">名称</th>
                      <th class="px-3 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                      <th class="px-3 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="(p,idx) in shownPoints" :key="p.id" class="hover:bg-blue-50 transition-colors">
                      <td class="px-3 py-3 whitespace-nowrap text-gray-900">{{ idx + 1 }}</td>
                      <td class="px-3 py-3">
                        <div class="font-medium text-gray-900">{{ p.name }}</div>
                        <div class="text-sm text-gray-500">{{ p.type || '未分类' }}</div>
                      </td>
                      <td class="px-3 py-3">
                        <span v-if="coordOf(p)" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">已定位</span>
                        <span v-else class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">未定位</span>
                      </td>
                      <td class="px-3 py-3 whitespace-nowrap">
                        <div class="flex gap-2">
                          <button class="px-3 py-1.5 text-sm bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors" 
                                  :class="!coordOf(p) ? 'opacity-50 cursor-not-allowed' : ''"
                                  :disabled="!coordOf(p)" @click="focusPoint(p)">定位</button>
                          <button class="px-3 py-1.5 text-sm bg-amber-600 text-white rounded-lg hover:bg-amber-700 transition-colors" 
                                  @click="openEdit(p)">编辑坐标</button>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="!shownPoints.length">
                      <td colspan="4" class="px-3 py-8 text-center text-gray-500">没有匹配的监测点</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div class="mt-4 p-3 bg-blue-50 rounded-lg border border-blue-200">
              <div class="text-xs text-blue-800">
                <p><strong>使用说明：</strong></p>
                <p>• 坐标格式：<code class="bg-blue-100 px-1 py-0.5 rounded">纬度,经度</code>，如 <code class="bg-blue-100 px-1 py-0.5 rounded">39.9042,116.4074</code></p>
                <p>• 右键地图可直接在该处"新增监测点"</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 地图区域 -->
        <div class="col-span-12 lg:col-span-8">
          <div class="bg-white rounded-lg shadow-xl p-4">
            <div class="flex items-center gap-2 mb-4">
              <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-1.447-.894L15 4m0 13V4m-6 3l6-3"></path>
              </svg>
              <h2 class="text-lg font-semibold text-gray-900">地理位置分布</h2>
            </div>
            <div class="bg-gray-100 rounded-lg border border-gray-200 overflow-hidden">
              <div ref="mapEl" class="w-full h-[640px]"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 编辑坐标弹窗 -->
  <div
      v-if="editor.show"
      :class="['fixed inset-0 bg-black/50 flex items-center justify-center', editor.pickOnMap ? 'pointer-events-none z-[10000]' : 'z-[10000]']"
  >
    <div class="bg-white rounded-lg shadow-2xl p-6 w-[520px] pointer-events-auto">
      <div class="flex items-center gap-3 mb-4">
        <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
          <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-900">编辑坐标 - {{ editor.point?.name }}</h3>
      </div>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">纬度 (lat) -90~90</label>
          <input 
            v-model.number="editor.lat" 
            type="number" 
            step="0.000001" 
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            placeholder="例: 39.9042"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">经度 (lon) -180~180</label>
          <input 
            v-model.number="editor.lon" 
            type="number" 
            step="0.000001" 
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
            placeholder="例: 116.4074"
          />
        </div>
      </div>

      <div class="flex items-center mt-4 p-3 bg-blue-50 rounded-lg border border-blue-200">
        <label class="inline-flex items-center gap-3 cursor-pointer">
          <input type="checkbox" v-model="editor.pickOnMap" class="rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
          <span class="text-sm text-blue-800 font-medium">地图拾取模式（勾选后点击地图即可填充坐标）</span>
        </label>
      </div>

      <div class="mt-6 flex justify-end gap-3">
        <button 
          class="px-4 py-2 text-gray-600 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
          @click="closeEdit"
        >
          取消
        </button>
        <button 
          class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          @click="saveEdit"
        >
          保存坐标
        </button>
      </div>
    </div>
  </div>

  <!-- 右键新增监测点弹窗 -->
  <div v-if="adder.show" class="fixed inset-0 bg-black/50 flex items-center justify-center z-[10000]">
    <div class="bg-white rounded-lg shadow-2xl p-6 w-[520px]">
      <div class="flex items-center gap-3 mb-4">
        <div class="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
          <svg class="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-900">在地图处新增监测点</h3>
      </div>
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">名称</label>
          <input 
            v-model="adder.name" 
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors" 
            placeholder="请输入站点名"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">类型</label>
          <input 
            v-model="adder.type" 
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors" 
            placeholder="如 WATER_LEVEL/RAINFALL"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">纬度</label>
          <input 
            v-model.number="adder.lat" 
            type="number" 
            step="0.000001" 
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors"
            placeholder="例: 39.9042"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">经度</label>
          <input 
            v-model.number="adder.lon" 
            type="number" 
            step="0.000001" 
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors"
            placeholder="例: 116.4074"
          />
        </div>
        <div class="col-span-2">
          <label class="block text-sm font-medium text-gray-700 mb-2">流域（可选）</label>
          <input 
            v-model="adder.riverBasin" 
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors"
            placeholder="请输入所属流域"
          />
        </div>
      </div>
      <div class="mt-6 flex justify-end gap-3">
        <button 
          class="px-4 py-2 text-gray-600 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
          @click="adder.show=false"
        >
          取消
        </button>
        <button 
          class="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
          @click="saveAdd"
        >
          创建监测点
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, computed } from 'vue'
import request from '../utils/request'
import * as L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import 'leaflet.markercluster'
import 'leaflet.markercluster/dist/MarkerCluster.css'
import 'leaflet.markercluster/dist/MarkerCluster.Default.css'

// 解决默认图标无法显示的问题（Vite 环境）
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'
L.Icon.Default.mergeOptions({
  iconRetinaUrl: markerIcon2x,
  iconUrl: markerIcon,
  shadowUrl: markerShadow
})

// --- 状态 ---
const rawPoints = ref([])      // 全部监测点
const keyword = ref('')
const mapEl = ref(null)
let map = null
let tileLayer = null
let clusterGroup = null        // Marker 聚合图层
const markers = new Map()      // id -> L.Marker

// 过滤
const filterOnlyOnMap = ref(false)

// 编辑弹窗
const editor = reactive({
  show: false,
  point: null,
  lat: null,
  lon: null,
  pickOnMap: false,
})

// 新增弹窗
const adder = reactive({
  show: false,
  name: '',
  type: '',
  lat: null,
  lon: null,
  riverBasin: '',
})

// 计算属性：根据关键词过滤出的监测点
const shownPoints = computed(() => {
  if (!keyword.value.trim()) return rawPoints.value
  const kw = keyword.value.toLowerCase()
  return rawPoints.value.filter(p => {
    const name = (p.name || '').toLowerCase()
    const type = (p.type || '').toLowerCase()
    const basin = (p.riverBasin || '').toLowerCase()
    return name.includes(kw) || type.includes(kw) || basin.includes(kw)
  })
})

// --- 方法 ---
function coordOf(point) {
  if (!point.location) return null
  const [lat, lon] = point.location.split(',').map(s => parseFloat(s.trim()))
  if (isNaN(lat) || isNaN(lon)) return null
  return [lat, lon]
}

async function reload() {
  try {
    const { data: resp } = await request.get('/api/points')
    if (resp.code === 0) {
      rawPoints.value = resp.data || []
      updateMap()
    }
  } catch (error) {
    console.error('加载监测点失败:', error)
  }
}

function toggleFilterOnly() {
  filterOnlyOnMap.value = !filterOnlyOnMap.value
  updateMap()
}

function applyFilter() {
  updateMap()
}

function updateMap() {
  if (!clusterGroup) return

  clusterGroup.clearLayers()
  markers.clear()

  const pointsToShow = filterOnlyOnMap.value ? shownPoints.value : rawPoints.value

  pointsToShow.forEach(point => {
    const coord = coordOf(point)
    if (!coord) return

    const [lat, lon] = coord
    const marker = L.marker([lat, lon])
      .bindPopup(`
        <div style="min-width: 200px;">
          <h4 style="margin: 0 0 8px 0; color: #1f2937;">${point.name}</h4>
          <p style="margin: 2px 0; font-size: 12px; color: #6b7280;">类型: ${point.type || '未分类'}</p>
          <p style="margin: 2px 0; font-size: 12px; color: #6b7280;">坐标: ${lat.toFixed(6)}, ${lon.toFixed(6)}</p>
          ${point.riverBasin ? `<p style="margin: 2px 0; font-size: 12px; color: #6b7280;">流域: ${point.riverBasin}</p>` : ''}
        </div>
      `)

    clusterGroup.addLayer(marker)
    markers.set(point.id, marker)
  })
}

function focusPoint(point) {
  const coord = coordOf(point)
  if (!coord) return
  const [lat, lon] = coord
  map.setView([lat, lon], 15)
  
  const marker = markers.get(point.id)
  if (marker) {
    marker.openPopup()
  }
}

function openEdit(point) {
  const coord = coordOf(point) || [null, null]
  editor.show = true
  editor.point = point
  editor.lat = coord[0]
  editor.lon = coord[1]
  editor.pickOnMap = false
}

function closeEdit() {
  editor.show = false
  editor.point = null
  editor.lat = null
  editor.lon = null
  editor.pickOnMap = false
}

async function saveEdit() {
  if (!editor.point || editor.lat == null || editor.lon == null) {
    alert('请填写完整的坐标信息')
    return
  }

  try {
    const { data: resp } = await request.put(`/api/points/${editor.point.id}`, {
      ...editor.point,
      location: `${editor.lat},${editor.lon}`
    })
    
    if (resp.code === 0) {
      alert('坐标更新成功')
      closeEdit()
      reload()
    } else {
      alert(resp.msg || '更新失败')
    }
  } catch (error) {
    console.error('更新坐标失败:', error)
    alert('更新失败，请检查网络连接')
  }
}

async function saveAdd() {
  if (!adder.name || adder.lat == null || adder.lon == null) {
    alert('请填写完整的监测点信息')
    return
  }

  try {
    const { data: resp } = await request.post('/api/points', {
      name: adder.name,
      type: adder.type,
      location: `${adder.lat},${adder.lon}`,
      riverBasin: adder.riverBasin
    })
    
    if (resp.code === 0) {
      alert('监测点创建成功')
      adder.show = false
      adder.name = ''
      adder.type = ''
      adder.lat = null
      adder.lon = null
      adder.riverBasin = ''
      reload()
    } else {
      alert(resp.msg || '创建失败')
    }
  } catch (error) {
    console.error('创建监测点失败:', error)
    alert('创建失败，请检查网络连接')
  }
}

// --- 生命周期 ---
onMounted(async () => {
  // 初始化地图
  map = L.map(mapEl.value, {
    center: [39.9042, 116.4074], // 北京
    zoom: 10,
    zoomControl: true,
  })

  // 添加瓦片图层
  tileLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 18,
  })
  tileLayer.addTo(map)

  // 初始化聚合图层
  clusterGroup = L.markerClusterGroup({
    maxClusterRadius: 50,
    spiderfyOnMaxZoom: true,
    showCoverageOnHover: false,
    zoomToBoundsOnClick: true,
  })
  map.addLayer(clusterGroup)

  // 地图右键菜单
  map.on('contextmenu', (e) => {
    adder.lat = e.latlng.lat
    adder.lon = e.latlng.lng
    adder.show = true
  })

  // 编辑模式下的地图点击
  map.on('click', (e) => {
    if (editor.show && editor.pickOnMap) {
      editor.lat = e.latlng.lat
      editor.lon = e.latlng.lng
    }
  })

  await reload()
})

onBeforeUnmount(() => {
  if (map) {
    map.remove()
  }
})
</script>