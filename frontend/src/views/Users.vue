<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 px-4 py-6">
    <div class="max-w-6xl mx-auto">
      <!-- 页面标题 -->
      <div class="mb-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
            </svg>
          </div>
          <h1 class="text-2xl font-bold text-gray-900">用户管理</h1>
        </div>
        <p class="text-gray-600">管理系统用户账号，分配角色权限</p>
      </div>

      <!-- 主内容卡片 -->
      <div class="bg-white rounded-lg shadow-xl p-6">
        <!-- 操作栏 -->
        <div class="flex items-center justify-between mb-6">
          <div class="flex items-center gap-3">
            <h2 class="text-lg font-semibold text-gray-900">用户列表</h2>
            <span class="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded-full">
              共 {{ list.length }} 个用户
            </span>
          </div>
          <button 
            class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-3 rounded-lg transition-colors flex items-center gap-2 shadow-sm" 
            @click="openAdd"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
            </svg>
            新增用户
          </button>
        </div>

        <!-- 用户表格 -->
        <div class="overflow-hidden rounded-lg border border-gray-200">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">用户信息</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">角色权限</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="u in list" :key="u.id" class="hover:bg-gray-50 transition-colors">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center gap-3">
                    <div class="flex-shrink-0 h-10 w-10">
                      <div class="h-10 w-10 rounded-full flex items-center justify-center" 
                           :class="getUserAvatarClass(u.role)">
                        <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                        </svg>
                      </div>
                    </div>
                    <div>
                      <div class="text-sm font-medium text-gray-900">{{ u.username }}</div>
                      <div class="text-sm text-gray-500">ID: {{ u.id }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="getRoleClass(u.role)">
                    <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" v-if="u.role === 'ADMIN'">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.031 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
                    </svg>
                    <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" v-else>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    {{ getRoleLabel(u.role) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                    <span class="w-1.5 h-1.5 bg-green-400 rounded-full mr-1.5"></span>
                    活跃
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center gap-2">
                    <button 
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-blue-600 bg-blue-100 hover:bg-blue-200 rounded-md transition-colors" 
                      @click="openEdit(u)"
                    >
                      <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                      </svg>
                      编辑
                    </button>
                    <button 
                      class="inline-flex items-center px-3 py-1 text-xs font-medium text-red-600 bg-red-100 hover:bg-red-200 rounded-md transition-colors" 
                      @click="remove(u)"
                      :disabled="u.role === 'ADMIN' && isLastAdmin(u)"
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
      <div class="bg-white rounded-lg shadow-2xl w-full max-w-md max-h-[90vh] overflow-y-auto">
        <!-- 弹窗头部 -->
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center gap-3">
            <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
              <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900">{{ form.id ? '编辑用户' : '新增用户' }}</h3>
          </div>
        </div>
        
        <!-- 弹窗内容 -->
        <div class="px-6 py-4">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">用户名</label>
              <div class="relative">
                <input 
                  v-model="form.username" 
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors pl-10" 
                  placeholder="请输入用户名"
                  :disabled="form.id"
                />
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                  </svg>
                </div>
              </div>
              <p class="text-xs text-gray-500 mt-1" v-if="form.id">用户名创建后不可修改</p>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                {{ form.id ? '新密码' : '密码' }}
                <span class="text-red-500" v-if="!form.id">*</span>
              </label>
              <div class="relative">
                <input 
                  v-model="form.password" 
                  type="password" 
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors pl-10" 
                  :placeholder="form.id ? '留空表示不修改密码' : '请输入密码'"
                />
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2a3 3 0 11-6 0v-2c0-.83.67-1.5 1.5-1.5h3c.83 0 1.5.67 1.5 1.5z"></path>
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12V7a4 4 0 00-8 0v5l8 0z"></path>
                  </svg>
                </div>
              </div>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">角色权限</label>
              <select 
                v-model="form.role" 
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
              >
                <option value="ADMIN">管理员 - 拥有所有权限</option>
                <option value="USER">普通用户 - 查看和操作权限</option>
              </select>
            </div>
            
            <!-- 权限说明 -->
            <div class="bg-gray-50 rounded-lg p-4">
              <h4 class="text-sm font-medium text-gray-700 mb-2">权限说明</h4>
              <div class="space-y-1 text-xs text-gray-600">
                <div class="flex items-center gap-2">
                  <svg class="w-3 h-3 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.031 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
                  </svg>
                  <span><strong>管理员</strong> - 用户管理、系统配置、所有功能</span>
                </div>
                <div class="flex items-center gap-2">
                  <svg class="w-3 h-3 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                  </svg>
                  <span><strong>普通用户</strong> - 监测数据查看、预警查看</span>
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
const show = ref(false)
const form = reactive({ id: null, username: '', password: '', role: 'ADMIN' })

// 获取用户头像样式
const getUserAvatarClass = (role) => {
  return role === 'ADMIN' ? 'bg-blue-600' : 'bg-green-600'
}

// 获取角色样式
const getRoleClass = (role) => {
  const roleMap = {
    'ADMIN': 'bg-blue-100 text-blue-800',
    'USER': 'bg-green-100 text-green-800'
  }
  return roleMap[role] || 'bg-gray-100 text-gray-800'
}

// 获取角色标签
const getRoleLabel = (role) => {
  const labelMap = {
    'ADMIN': '管理员',
    'USER': '普通用户'
  }
  return labelMap[role] || role
}

// 检查是否是最后一个管理员
const isLastAdmin = (user) => {
  if (user.role !== 'ADMIN') return false
  const adminCount = list.value.filter(u => u.role === 'ADMIN').length
  return adminCount <= 1
}

const load = async () => {
  try {
    const { data } = await request.get('/api/users')
    if(data.code === 0) {
      // 按id从小到大排序
      list.value = (data.data || []).sort((a, b) => a.id - b.id)
    }
  } catch (error) {
    console.error('加载用户失败:', error)
  }
}

const openAdd = () => {
  Object.assign(form, { id: null, username: '', password: '', role: 'USER' })
  show.value = true
}

const openEdit = (u) => {
  Object.assign(form, { ...u, password: '' }) // 编辑时密码置空
  show.value = true
}

const save = async () => {
  if (!form.username) {
    alert('请输入用户名')
    return
  }
  
  if (!form.id && !form.password) {
    alert('新用户必须设置密码')
    return
  }
  
  try {
    const submitData = { ...form }
    // 如果是编辑且密码为空，则不提交密码字段
    if (form.id && !form.password) {
      delete submitData.password
    }
    
    if (form.id) {
      const { data } = await request.put('/api/users/' + form.id, submitData)
      if (data.code === 0) { 
        show.value = false
        load() 
      } else {
        alert(data.msg || '更新失败')
      }
    } else {
      const { data } = await request.post('/api/users', submitData)
      if (data.code === 0) { 
        show.value = false
        load() 
      } else {
        alert(data.msg || '创建失败')
      }
    }
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败，请重试')
  }
}

const remove = async (u) => {
  if (u.role === 'ADMIN' && isLastAdmin(u)) {
    alert('不能删除最后一个管理员账号')
    return
  }
  
  if (!confirm(`确认删除用户 "${u.username}"？`)) return
  
  try {
    const { data } = await request.delete('/api/users/' + u.id)
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
