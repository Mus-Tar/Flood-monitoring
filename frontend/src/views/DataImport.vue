<template>
  <div class="bg-white p-4 rounded-lg shadow space-y-6">
    <h2 class="text-lg font-semibold">数据导入（CSV / Excel）</h2>

    <div class="bg-yellow-50 border border-yellow-200 text-yellow-800 p-3 rounded space-y-2">
      <p class="text-sm">请先在“监测点管理”新增站点，再在此选择站点导入其时序数据。</p>
      <p class="text-sm">
        必填列：<code>timestamp, water_level, rainfall</code>；可选列：<code>flow</code>；
        时间格式 <code>yyyy-MM-dd HH:mm:ss</code>。
      </p>

      <details class="text-sm">
        <summary class="cursor-pointer">点击展开：CSV 模板（复制到本地文件，例如 monitor_data_template.csv）</summary>
        <pre class="bg-white border rounded p-2 overflow-auto text-xs mt-2">timestamp,water_level,rainfall,flow
2025-09-10 08:00:00,1.23,0.00,0.56
2025-09-10 09:00:00,1.35,2.40,0.61
2025-09-10 10:00:00,1.50,3.20,0.73
2025-09-10 11:00:00,1.62,1.10,0.69
</pre>
      </details>
    </div>

    <div class="grid md:grid-cols-3 gap-4">
      <div>
        <label class="block text-sm mb-1">选择监测点</label>
        <select v-model="pointId" class="w-full border rounded px-3 py-2">
          <option disabled value="">请选择监测点</option>
          <option v-for="p in points" :key="p.id" :value="p.id">
            {{ p.name }}（{{ p.type || '未分类' }}）
          </option>
        </select>
      </div>
      <div class="md:col-span-2">
        <label class="block text-sm mb-1">选择文件（.csv 或 .xlsx）</label>
        <input type="file" @change="onFile" class="w-full"/>
      </div>
    </div>

    <div>
      <button class="bg-blue-600 text-white px-4 py-2 rounded" :disabled="!file || !pointId" @click="upload">上传并导入</button>
    </div>

    <div v-if="result" class="mt-4">
      <h3 class="font-semibold">导入结果</h3>
      <ul class="list-disc pl-6 text-sm">
        <li>总行数：{{ result.total }}</li>
        <li>成功：{{ result.success }}</li>
        <li>失败：{{ result.failed }}</li>
      </ul>
      <div v-if="(result.errors||[]).length" class="mt-2">
        <p class="text-sm text-red-600">错误示例（最多10条）：</p>
        <ul class="list-disc pl-6 text-xs">
          <li v-for="(e,i) in result.errors" :key="i">{{ e }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'

const points = ref([])
const pointId = ref('')
const file = ref(null)
const result = ref(null)

const loadPoints = async () => {
  const { data } = await request.get('/api/points')
  if (data.code === 0) points.value = data.data || []
}

const onFile = (e) => { file.value = e.target.files[0] }

const upload = async () => {
  const fd = new FormData()
  fd.append('file', file.value)
  const { data } = await request.post('/api/monitor-data/import', fd, {
    params: { pointId: pointId.value },
    headers: { 'Content-Type': 'multipart/form-data' }
  })
  if (data.code === 0) {
    result.value = data.data
    alert('导入完成！')
  } else {
    alert(data.msg || '导入失败')
  }
}

onMounted(loadPoints)
</script>
