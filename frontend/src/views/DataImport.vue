<<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 px-        </div>
      </div>

      <!-- 数据导入卡片 -->6">
    <div class="max-w-6xl mx-auto">
      <!-- 页面标题 -->
      <div class="mb-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M9 19l3 3m0 0l3-3m-3 3V10"></path>
            </svg>
          </div>
          <h1 class="text-2xl font-bold text-gray-900">数据导入</h1>
        </div>
        <p class="text-gray-600">批量导入监测站点的时序数据，支持 CSV 和 Excel 格式</p>
      </div>

      <!-- 使用说明卡片 -->
      <div class="bg-white rounded-lg shadow-xl mb-6">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center gap-2">
            <svg class="w-5 h-5 text-amber-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
            <h2 class="text-lg font-semibold text-gray-900">导入说明</h2>
          </div>
        </div>
        
        <div class="p-6">
          <div class="grid md:grid-cols-2 gap-6">
            <div>
              <h3 class="font-medium text-gray-900 mb-3">操作步骤</h3>
              <ol class="list-decimal list-inside space-y-2 text-sm text-gray-600">
                <li>请先在"监测点管理"中创建监测点</li>
                <li>选择要导入数据的监测点</li>
                <li>选择符合格式的 CSV 或 Excel 文件</li>
                <li>点击"上传并导入"开始处理</li>
              </ol>
            </div>
            
            <div>
              <h3 class="font-medium text-gray-900 mb-3">格式要求</h3>
              <div class="space-y-2 text-sm">
                <div class="flex items-center gap-2">
                  <span class="w-2 h-2 bg-green-400 rounded-full"></span>
                  <span><strong>必填列:</strong> <code class="bg-gray-100 px-1 rounded">timestamp, water_level, rainfall</code></span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="w-2 h-2 bg-blue-400 rounded-full"></span>
                  <span><strong>可选列:</strong> <code class="bg-gray-100 px-1 rounded">flow</code></span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="w-2 h-2 bg-purple-400 rounded-full"></span>
                  <span><strong>时间格式:</strong> <code class="bg-gray-100 px-1 rounded">yyyy-MM-dd HH:mm:ss</code></span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- CSV 模板 -->
          <div class="mt-6">
            <details class="group">
              <summary class="cursor-pointer flex items-center gap-2 text-sm font-medium text-gray-700 hover:text-blue-600">
                <svg class="w-4 h-4 transition-transform group-open:rotate-90" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                </svg>
                点击查看 CSV 模板示例
              </summary>
              <div class="mt-3 bg-gray-50 rounded-lg p-4">
                <div class="flex items-center justify-between mb-2">
                  <span class="text-xs text-gray-500">CSV 格式示例</span>
                  <button 
                    @click="copyTemplate" 
                    class="text-xs text-blue-600 hover:text-blue-700 flex items-center gap-1"
                  >
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
                    </svg>
                    复制
                  </button>
                </div>
                <pre class="text-xs bg-white border rounded p-3 overflow-auto font-mono">timestamp,water_level,rainfall,flow
2025-09-10 08:00:00,1.23,0.00,0.56
2025-09-10 09:00:00,1.35,2.40,0.61
2025-09-10 10:00:00,1.50,3.20,0.73
2025-09-10 11:00:00,1.62,1.10,0.69</pre>
              </div>
            </details>
          </div>
        </div>
      </div>
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

      <!-- 数据导入卡片 -->
      <div class="bg-white rounded-lg shadow-xl">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center gap-2">
            <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M9 19l3 3m0 0l3-3m-3 3V10"></path>
            </svg>
            <h2 class="text-lg font-semibold text-gray-900">上传数据文件</h2>
          </div>
        </div>
        
        <div class="p-6">
          <div class="grid md:grid-cols-3 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">选择监测点</label>
              <select 
                v-model="pointId" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
              >
                <option disabled value="">请选择监测点</option>
                <option v-for="p in points" :key="p.id" :value="p.id">
                  {{ p.name }} ({{ p.type || '未分类' }})
                </option>
              </select>
            </div>
            
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-2">选择文件</label>
              <!-- 文件上传区域 -->
              <div class="relative">
                <input 
                  type="file" 
                  @change="onFile" 
                  accept=".csv,.xlsx,.xls"
                  class="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10"
                  ref="fileInput"
                />
                <div class="border-2 border-dashed border-gray-300 rounded-lg p-6 hover:border-blue-400 transition-colors"
                     :class="{ 'border-blue-400 bg-blue-50': file, 'hover:bg-gray-50': !file }">
                  <div class="text-center">
                    <svg class="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48">
                      <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                    <div class="mt-4">
                      <p class="text-sm text-gray-600">
                        <span class="font-medium text-blue-600 hover:text-blue-500">点击选择文件</span>
                        或拖拽文件到此处
                      </p>
                      <p class="text-xs text-gray-500 mt-1">支持 CSV, XLSX 格式，最大 10MB</p>
                      <div v-if="file" class="mt-3 flex items-center justify-center gap-2 text-sm text-blue-600">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                        已选择: {{ file.name }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="mt-6 flex items-center gap-4">
            <button 
              class="bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed text-white px-6 py-3 rounded-lg transition-colors flex items-center gap-2 font-medium" 
              :disabled="!file || !pointId || uploading" 
              @click="upload"
            >
              <svg v-if="uploading" class="animate-spin w-4 h-4" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M9 19l3 3m0 0l3-3m-3 3V10"></path>
              </svg>
              {{ uploading ? '正在导入...' : '上传并导入' }}
            </button>
            
            <button 
              v-if="file" 
              @click="clearFile" 
              class="text-gray-600 hover:text-gray-800 px-3 py-3 rounded-lg transition-colors flex items-center gap-2"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
              </svg>
              清除文件
            </button>
          </div>
        </div>
      </div>

      <!-- 导入结果 -->
      <div v-if="result" class="bg-white rounded-lg shadow-xl">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center gap-2">
            <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
            <h2 class="text-lg font-semibold text-gray-900">导入结果</h2>
          </div>
        </div>
        
        <div class="p-6">
          <div class="grid md:grid-cols-3 gap-6 mb-6">
            <div class="bg-blue-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-blue-600 mb-1">{{ result.total || 0 }}</div>
              <div class="text-sm text-blue-800">总处理行数</div>
            </div>
            <div class="bg-green-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-green-600 mb-1">{{ result.success || 0 }}</div>
              <div class="text-sm text-green-800">成功导入</div>
            </div>
            <div class="bg-red-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-red-600 mb-1">{{ result.failed || 0 }}</div>
              <div class="text-sm text-red-800">导入失败</div>
            </div>
          </div>
          
          <!-- 成功率显示 -->
          <div class="mb-6">
            <div class="flex items-center justify-between text-sm text-gray-600 mb-2">
              <span>成功率</span>
              <span>{{ Math.round((result.success / result.total) * 100) }}%</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2">
              <div 
                class="bg-green-600 h-2 rounded-full transition-all duration-500" 
                :style="{ width: Math.round((result.success / result.total) * 100) + '%' }"
              ></div>
            </div>
          </div>
          
          <!-- 错误详情 -->
          <div v-if="(result.errors || []).length" class="bg-red-50 border border-red-200 rounded-lg p-4">
            <div class="flex items-center gap-2 mb-3">
              <svg class="w-4 h-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
              </svg>
              <span class="text-sm font-medium text-red-800">错误详情 (显示前10条)</span>
            </div>
            <div class="space-y-1 max-h-32 overflow-y-auto">
              <div 
                v-for="(error, i) in result.errors.slice(0, 10)" 
                :key="i" 
                class="text-xs text-red-700 bg-white px-2 py-1 rounded border"
              >
                {{ error }}
              </div>
            </div>
            <div v-if="result.errors.length > 10" class="text-xs text-red-600 mt-2">
              还有 {{ result.errors.length - 10 }} 个错误未显示...
            </div>
          </div>
          
          <!-- 操作建议 -->
          <div v-if="result.failed > 0" class="mt-4 bg-amber-50 border border-amber-200 rounded-lg p-4">
            <div class="flex items-start gap-2">
              <svg class="w-4 h-4 text-amber-600 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
              <div class="text-sm text-amber-800">
                <p class="font-medium mb-1">处理建议：</p>
                <ul class="list-disc list-inside space-y-1">
                  <li>检查失败行的数据格式是否正确</li>
                  <li>确认时间格式为 yyyy-MM-dd HH:mm:ss</li>
                  <li>检查数值字段是否包含非数字字符</li>
                  <li>修正后可重新导入</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 数据删除功能 -->
      <div class="bg-white rounded-lg shadow-xl">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center gap-2">
            <svg class="w-5 h-5 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
            </svg>
            <h2 class="text-lg font-semibold text-gray-900">数据删除</h2>
          </div>
          <p class="text-sm text-gray-600 mt-1">批量删除监测点的所有历史数据，操作不可恢复</p>
        </div>
        
        <div class="p-6">
          <!-- 警告提示 -->
          <div class="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
            <div class="flex items-start gap-3">
              <svg class="w-5 h-5 text-red-600 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
              </svg>
              <div>
                <h3 class="text-sm font-medium text-red-800 mb-1">⚠️ 重要警告</h3>
                <p class="text-sm text-red-700">此操作将永久删除选中监测点的所有历史数据，删除后无法恢复。请谨慎操作！</p>
              </div>
            </div>
          </div>

          <!-- 监测点选择表格 -->
          <div class="mb-6">
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center gap-4">
                <button 
                  @click="deleteTableExpanded = !deleteTableExpanded"
                  class="flex items-center gap-2 text-sm font-medium text-gray-900 hover:text-blue-600 transition-colors"
                >
                  <svg class="w-4 h-4 transition-transform duration-200" :class="{ 'rotate-90': deleteTableExpanded }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                  </svg>
                  选择要删除数据的监测点
                </button>
                <button 
                  v-if="deleteTableExpanded"
                  @click="loadDataCounts" 
                  class="text-xs text-blue-600 hover:text-blue-700 flex items-center gap-1"
                  :disabled="loadingDataCounts"
                >
                  <svg class="w-3 h-3" :class="{ 'animate-spin': loadingDataCounts }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                  </svg>
                  刷新数据量
                </button>
              </div>
              <div class="text-xs text-gray-500">
                已选择 {{ selectedPointIds.length }} 个监测点
                <span v-if="!deleteTableExpanded && selectedPointIds.length > 0" class="ml-2 px-2 py-1 bg-blue-100 text-blue-700 rounded-full text-xs">
                  将删除 {{ getTotalDataCount() }} 条记录
                </span>
              </div>
            </div>

            <!-- 可折叠的表格内容 -->
            <div v-show="deleteTableExpanded" class="overflow-hidden transition-all duration-300">
              <div class="overflow-hidden rounded-lg border border-gray-200">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead class="bg-gray-50">
                    <tr>
                      <th class="px-4 py-3 text-left">
                        <input 
                          type="checkbox" 
                          :checked="selectedPointIds.length === pointsWithDataCount.length && pointsWithDataCount.length > 0"
                          :indeterminate="selectedPointIds.length > 0 && selectedPointIds.length < pointsWithDataCount.length"
                          @change="toggleSelectAll"
                          class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                        />
                      </th>
                      <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                      <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">名称</th>
                      <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型</th>
                      <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">数据量</th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="point in pointsWithDataCount" :key="point.id" class="hover:bg-gray-50">
                      <td class="px-4 py-4 whitespace-nowrap">
                        <input 
                          type="checkbox" 
                          :value="point.id"
                          v-model="selectedPointIds"
                          class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                        />
                      </td>
                      <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-900">{{ point.id }}</td>
                      <td class="px-4 py-4 whitespace-nowrap">
                        <div class="text-sm font-medium text-gray-900">{{ point.name }}</div>
                      </td>
                      <td class="px-4 py-4 whitespace-nowrap">
                        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium" 
                              :class="getTypeClass(point.type)">
                          {{ getTypeLabel(point.type) }}
                        </span>
                      </td>
                      <td class="px-4 py-4 whitespace-nowrap">
                        <div class="flex items-center gap-2">
                          <span class="text-sm text-gray-900">
                            {{ point.dataCount !== undefined ? point.dataCount : '-' }}
                          </span>
                          <span v-if="point.dataCount !== undefined && point.dataCount > 0" class="text-xs text-gray-500">条记录</span>
                          <span v-if="point.dataCount === 0" class="text-xs text-gray-400">无数据</span>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- 折叠状态下的简要信息 -->
            <div v-if="!deleteTableExpanded && pointsWithDataCount.length > 0" class="bg-gray-50 rounded-lg p-4 border border-gray-200">
              <div class="flex items-center justify-between">
                <div class="text-sm text-gray-600">
                  共 {{ pointsWithDataCount.length }} 个监测点可选择
                  <span v-if="selectedPointIds.length > 0" class="text-blue-600 font-medium">
                    (已选择 {{ selectedPointIds.length }} 个)
                  </span>
                </div>
                <button 
                  @click="deleteTableExpanded = true"
                  class="text-xs text-blue-600 hover:text-blue-700 flex items-center gap-1"
                >
                  <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                  </svg>
                  展开选择
                </button>
              </div>
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="flex items-center gap-4">
            <button 
              @click="showDeleteConfirm"
              :disabled="selectedPointIds.length === 0 || deleting"
              class="bg-red-600 hover:bg-red-700 disabled:bg-gray-400 disabled:cursor-not-allowed text-white px-6 py-3 rounded-lg transition-colors flex items-center gap-2 font-medium"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
              </svg>
              删除选中数据 ({{ selectedPointIds.length }})
            </button>
            
            <div class="text-sm text-gray-600">
              <span v-if="getTotalDataCount() > 0">
                将删除约 <strong class="text-red-600">{{ getTotalDataCount() }}</strong> 条历史记录
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

    <!-- 删除确认对话框 -->
    <div v-if="deleteConfirmVisible" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4">
      <div class="bg-white rounded-lg shadow-2xl w-full max-w-md">
        <!-- 对话框头部 -->
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center gap-3">
            <div class="w-8 h-8 bg-red-100 rounded-full flex items-center justify-center">
              <svg class="w-4 h-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900">确认删除数据</h3>
          </div>
        </div>
        
        <!-- 对话框内容 -->
        <div class="px-6 py-4">
          <div class="mb-4">
            <p class="text-sm text-gray-700 mb-3">您即将删除以下监测点的所有历史数据：</p>
            <div class="bg-gray-50 rounded-lg p-3 max-h-32 overflow-y-auto">
              <div v-for="point in getSelectedPoints()" :key="point.id" class="flex justify-between items-center py-1">
                <span class="text-sm font-medium">{{ point.name }}</span>
                <span class="text-xs text-red-600">{{ point.dataCount || 0 }} 条记录</span>
              </div>
            </div>
          </div>
          
          <div class="bg-red-50 border border-red-200 rounded-lg p-3 mb-4">
            <div class="flex items-center gap-2 text-sm text-red-800">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
              </svg>
              <span>
                总计将删除 <strong>{{ getTotalDataCount() }}</strong> 条历史记录，此操作不可恢复！
              </span>
            </div>
          </div>
          
          <div class="text-xs text-gray-500">
            <p>• 删除操作仅影响监测数据，不会删除监测点配置</p>
            <p>• 删除后可能影响相关的阈值配置和预测模型分析</p>
          </div>
        </div>
        
        <!-- 对话框底部 -->
        <div class="px-6 py-4 border-t border-gray-200 flex justify-end gap-3">
          <button 
            class="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors" 
            @click="deleteConfirmVisible = false"
            :disabled="deleting"
          >
            取消
          </button>
          <button 
            class="px-4 py-2 bg-red-600 hover:bg-red-700 disabled:bg-red-400 text-white rounded-lg transition-colors flex items-center gap-2"
            @click="confirmDelete"
            :disabled="deleting"
          >
            <svg v-if="deleting" class="animate-spin w-4 h-4" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
            </svg>
            {{ deleting ? '删除中...' : '确认删除' }}
          </button>
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
const uploading = ref(false)
const fileInput = ref(null)

// 数据删除相关状态
const pointsWithDataCount = ref([])
const selectedPointIds = ref([])
const deleteConfirmVisible = ref(false)
const deleting = ref(false)
const loadingDataCounts = ref(false)
const deleteTableExpanded = ref(false) // 修改为默认折叠状态

const csvTemplate = `timestamp,water_level,rainfall,flow
2025-09-10 08:00:00,1.23,0.00,0.56
2025-09-10 09:00:00,1.35,2.40,0.61
2025-09-10 10:00:00,1.50,3.20,0.73
2025-09-10 11:00:00,1.62,1.10,0.69`

const loadPoints = async () => {
  try {
    const { data } = await request.get('/api/points')
    if (data.code === 0) {
      // 按id从小到大排序
      points.value = (data.data || []).sort((a, b) => a.id - b.id)
      pointsWithDataCount.value = [...points.value]
      // 初始加载数据量
      await loadDataCounts()
    }
  } catch (error) {
    console.error('加载监测点失败:', error)
  }
}

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

// 加载数据量统计
const loadDataCounts = async () => {
  if (pointsWithDataCount.value.length === 0) return
  
  loadingDataCounts.value = true
  try {
    const pointIds = pointsWithDataCount.value.map(p => p.id)
    const { data } = await request.get('/api/monitor-data/count-by-points', {
      params: { pointIds: pointIds.join(',') }
    })
    
    if (data.code === 0) {
      pointsWithDataCount.value = pointsWithDataCount.value.map(point => ({
        ...point,
        dataCount: data.data.pointCounts[point.id] || 0
      }))
    }
  } catch (error) {
    console.error('加载数据量失败:', error)
    // 如果失败，设置默认值
    pointsWithDataCount.value = pointsWithDataCount.value.map(point => ({
      ...point,
      dataCount: 0
    }))
  } finally {
    loadingDataCounts.value = false
  }
}

// 全选/取消全选
const toggleSelectAll = () => {
  if (selectedPointIds.value.length === pointsWithDataCount.value.length) {
    selectedPointIds.value = []
  } else {
    selectedPointIds.value = pointsWithDataCount.value.map(p => p.id)
  }
}

// 获取总数据量
const getTotalDataCount = () => {
  return selectedPointIds.value.reduce((total, pointId) => {
    const point = pointsWithDataCount.value.find(p => p.id === pointId)
    return total + (point?.dataCount || 0)
  }, 0)
}

// 获取选中的监测点信息
const getSelectedPoints = () => {
  return pointsWithDataCount.value.filter(p => selectedPointIds.value.includes(p.id))
}

// 显示删除确认对话框
const showDeleteConfirm = () => {
  if (selectedPointIds.value.length === 0) return
  deleteConfirmVisible.value = true
}

// 确认删除
const confirmDelete = async () => {
  if (selectedPointIds.value.length === 0) return
  
  deleting.value = true
  try {
    const { data } = await request.delete('/api/monitor-data/batch', {
      data: selectedPointIds.value
    })
    
    if (data.code === 0) {
      const result = data.data
      alert(`删除完成！\n` +
            `共删除 ${result.totalDeleted} 条记录\n` +
            `影响 ${result.affectedPoints} 个监测点`)
      
      // 重置状态
      selectedPointIds.value = []
      deleteConfirmVisible.value = false
      
      // 重新加载数据量
      await loadDataCounts()
    } else {
      alert(data.msg || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败，请检查网络连接')
  } finally {
    deleting.value = false
  }
}

const onFile = (e) => { 
  file.value = e.target.files[0]
  // 重置结果
  result.value = null
}

const clearFile = () => {
  file.value = null
  if (fileInput.value) {
    fileInput.value.value = ''
  }
  result.value = null
}

const copyTemplate = async () => {
  try {
    await navigator.clipboard.writeText(csvTemplate)
    // 可以添加一个临时的成功提示
    alert('CSV 模板已复制到剪贴板')
  } catch (err) {
    console.error('复制失败:', err)
    // 如果复制失败，可以创建一个临时的 textarea
    const textarea = document.createElement('textarea')
    textarea.value = csvTemplate
    document.body.appendChild(textarea)
    textarea.select()
    document.execCommand('copy')
    document.body.removeChild(textarea)
    alert('CSV 模板已复制到剪贴板')
  }
}

const upload = async () => {
  if (!file.value || !pointId.value) return
  
  uploading.value = true
  result.value = null
  
  try {
    const fd = new FormData()
    fd.append('file', file.value)
    
    const { data } = await request.post('/api/monitor-data/import', fd, {
      params: { pointId: pointId.value },
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    
    if (data.code === 0) {
      result.value = data.data
      if (data.data.failed === 0) {
        alert('数据导入完成！所有数据都已成功导入。')
      } else {
        alert(`数据导入完成！成功导入 ${data.data.success} 条记录，失败 ${data.data.failed} 条。`)
      }
    } else {
      alert(data.msg || '导入失败')
    }
  } catch (error) {
    console.error('导入失败:', error)
    alert('导入失败，请检查文件格式或网络连接')
  } finally {
    uploading.value = false
  }
}

onMounted(loadPoints)
</script>
