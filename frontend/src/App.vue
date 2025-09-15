<template>
  <div class="min-h-screen">
    <header class="bg-white shadow">
      <div class="mx-auto max-w-7xl px-4 py-4 flex items-center justify-between">
        <h1 class="text-lg font-semibold">Flood Monitoring Demo</h1>
        <nav class="space-x-4">
          <!-- 原有的 -->
          <router-link to="/users" class="text-blue-600 hover:underline">用户管理</router-link>
          <router-link to="/login" class="text-gray-600 hover:underline">登录</router-link>

          <!-- 新增的 -->
          <router-link to="/points" class="text-blue-600 hover:underline">监测点</router-link>
          <router-link to="/import" class="text-blue-600 hover:underline">数据导入</router-link>

          <router-link to="/thresholds" class="text-blue-600 hover:underline">阈值配置</router-link>
          <router-link to="/warnings" class="text-blue-600 hover:underline">预警列表</router-link>
          <router-link to="/history" class="text-blue-600 hover:underline">历史趋势</router-link>
          <router-link to="/gis" class="text-blue-600 hover:underline">GIS 地图</router-link>
          <router-link to="/forecast" class="text-blue-600 hover:underline">预测工作台</router-link>





        </nav>
      </div>
    </header>
    <main class="mx-auto max-w-7xl p-4">
      <router-view />
    </main>
  </div>
</template>
