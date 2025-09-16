# 洪水监测系统 - BUG修复与功能完善执行计划

## 📋 任务概述

本文档详细说明了需要解决的问题以及实现方案，包括：
1. **修复注册登录BUG**：解决注册用户无法登录的问题
2. **修复密码安全问题**：解决密码明文存储导致的安全警告
3. **实现权限控制**：区分管理员和普通用户的页面访问权限

## 🔍 问题分析

### 问题1：注册用户无法登录
**根本原因**：数据结构不一致
- **注册时**：用户数据保存格式为 `{username: {password, role, createdAt}}`
- **登录时**：查找格式为 `[{username, password, role}]`

**问题代码对比**：
```javascript
// 注册页面保存格式
registeredUsers[form.value.username] = {
  password: form.value.password,
  role: form.value.role,
  createdAt: new Date().toISOString()
}

// 登录页面查找格式  
const registeredUsers = JSON.parse(localStorage.getItem('registeredUsers') || '[]')
const registeredUser = registeredUsers.find(user => 
  user.username === form.username && user.password === form.password
)
```

### 问题2：密码安全问题
**根本原因**：密码明文存储
- 当前密码直接以明文形式存储在localStorage中
- 浏览器开发者工具可以直接查看到密码
- 这会触发浏览器的安全警告

### 问题3：缺乏权限控制
**当前状态**：所有用户都能访问所有页面
**需求**：区分管理员和普通用户权限

## 🛠️ 解决方案

### 解决方案1：修复注册登录BUG
**复杂度**：⭐ (简单)
**影响范围**：Register.vue, Login.vue

**修复内容**：
1. 统一数据存储格式为数组结构
2. 修改注册页面的保存逻辑
3. 确保登录页面的查找逻辑正确

**修改文件**：
- `frontend/src/views/Register.vue` - 修改保存逻辑
- `frontend/src/views/Login.vue` - 修改查找逻辑（如果需要）

### 解决方案2：修复密码安全问题
**复杂度**：⭐⭐ (中等)
**影响范围**：Register.vue, Login.vue, 新增密码工具类

**修复内容**：
1. 创建密码加密工具函数（使用SHA-256）
2. 注册时对密码进行哈希处理
3. 登录时对输入密码进行哈希后比较
4. 添加盐值增强安全性

**实现方式**：
```javascript
// 密码哈希函数
async function hashPassword(password, salt = '') {
  const encoder = new TextEncoder();
  const data = encoder.encode(password + salt);
  const hashBuffer = await crypto.subtle.digest('SHA-256', data);
  const hashArray = Array.from(new Uint8Array(hashBuffer));
  return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
}
```

### 解决方案3：实现权限控制系统
**复杂度**：⭐⭐⭐ (中等偏高)
**影响范围**：路由配置、多个视图组件

**实现内容**：

#### 3.1 权限定义
- **管理员 (ADMIN)**：可访问所有功能
  - 用户管理、监测点管理、数据导入、阈值配置
  - 预警管理、历史数据、GIS地图、预测工作台
- **普通用户 (USER)**：仅可访问只读功能
  - 监测点查看、历史数据查看、GIS地图查看
  - 不能访问：用户管理、数据导入、阈值配置、预测工作台

#### 3.2 技术实现
1. **路由守卫增强**：
   ```javascript
   // 页面权限配置
   const pagePermissions = {
     '/users': ['ADMIN'],
     '/import': ['ADMIN'], 
     '/thresholds': ['ADMIN'],
     '/forecast': ['ADMIN'],
     '/points': ['ADMIN', 'USER'],
     '/history': ['ADMIN', 'USER'],
     '/gis': ['ADMIN', 'USER'],
     '/warnings': ['ADMIN', 'USER']
   }
   ```

2. **创建权限检查组件**：
   ```vue
   <!-- PermissionWrapper.vue -->
   <template>
     <div v-if="hasPermission">
       <slot></slot>
     </div>
     <div v-else class="permission-denied">
       权限不足，请联系管理员
     </div>
   </template>
   ```

3. **导航菜单权限控制**：
   - 根据用户角色动态显示菜单项
   - 隐藏无权访问的功能入口

#### 3.3 用户体验优化
- 普通用户访问受限页面时显示友好的权限不足提示
- 在页面中隐藏普通用户不能使用的操作按钮
- 提供角色切换的管理界面（仅管理员可见）

## 📁 文件修改清单

### 新增文件
1. `frontend/src/utils/crypto.js` - 密码加密工具
2. `frontend/src/components/PermissionWrapper.vue` - 权限检查组件
3. `frontend/src/utils/permissions.js` - 权限配置和检查函数

### 修改文件
1. `frontend/src/views/Register.vue` - 修复数据格式，添加密码加密
2. `frontend/src/views/Login.vue` - 修复查找逻辑，添加密码验证
3. `frontend/src/router/index.js` - 增强路由守卫，添加权限检查
4. 各个需要权限控制的视图组件 (Users.vue, DataImport.vue, Thresholds.vue 等)

## ⏱️ 预估工作量

| 任务 | 预估时间 | 风险等级 |
|------|---------|----------|
| 修复注册登录BUG | 30分钟 | 低 |
| 实现密码加密 | 1小时 | 低 |
| 实现权限控制系统 | 2-3小时 | 中 |
| 测试与调试 | 1小时 | 低 |
| **总计** | **4.5-5.5小时** | **中** |

## 🧪 测试计划

### 功能测试
1. **注册登录流程**：
   - 注册新用户 → 登录验证 → 成功进入系统
   - 测试管理员和普通用户角色
   
2. **密码安全性**：
   - 检查localStorage中密码已加密
   - 验证登录时密码匹配逻辑
   
3. **权限控制**：
   - 管理员登录 → 访问所有页面 ✅
   - 普通用户登录 → 访问受限页面显示权限不足 ❌
   - 直接URL访问测试

### 安全测试
- 浏览器开发者工具检查密码存储
- 尝试绕过前端权限限制

## 🚀 部署建议

1. **备份当前代码**：执行修改前先备份
2. **渐进式部署**：先修复BUG，再实现权限控制
3. **用户通知**：如需要用户重新注册，提前通知

## ❓ 确认事项

请确认以下内容：

1. **修复优先级**：是否同意先修复注册登录BUG，再实现权限控制？
2. **权限方案**：上述权限分配是否符合需求？
3. **密码处理**：现有用户数据是否需要迁移？（建议清空重新注册）
4. **功能范围**：是否需要添加其他权限控制功能？

## 📋 执行检查清单

执行前请确认：
- [ ] 已备份当前项目代码
- [ ] 了解修改影响范围
- [ ] 确认权限分配方案
- [ ] 准备好测试账号数据

---

**请您review此执行计划，确认是否同意执行。如有任何疑问或需要调整的地方，请告知！**