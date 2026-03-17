# 洪水监测系统 API 接口文档

## 项目概述

本项目是一个基于 Spring Boot + Vue.js 的洪水监测系统，用于实时监测水位、降雨量等环境数据，并提供预警功能。

### 技术栈
- **后端**: Spring Boot 2.x, MyBatis-Plus, MySQL
- **前端**: Vue 3, Element Plus, Axios
- **数据库**: MySQL

### 服务器配置
- **后端地址**: http://localhost:8080
- **前端地址**: http://localhost:5173 (开发环境)

---

## 1. 认证相关接口 (AuthController)

### 1.1 用户登录
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/AuthController.java`
- **请求路径**: `POST /api/auth/login`
- **功能描述**: 用户身份验证，返回token和用户信息
- **使用场景**: 
  - 前端登录页面 (`frontend/src/views/Login.vue`)
  - 系统入口认证

#### 请求参数
```json
{
  "username": "string",  // 用户名
  "password": "string"   // 密码
}
```

#### 响应示例
```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "token": "demo-token",
    "username": "admin",
    "role": "ADMIN"
  }
}
```

#### 错误响应
```json
{
  "code": 500,
  "msg": "用户名或密码错误"
}
```

### 1.2 用户注册
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/AuthController.java`
- **请求路径**: `POST /api/auth/register`
- **功能描述**: 新用户注册，支持输入验证和角色分配
- **使用场景**: 前端注册页面 (`frontend/src/views/Register.vue`)

#### 请求参数
```json
{
  "username": "string",  // 用户名（至少3个字符）
  "password": "string",  // 密码（至少6个字符）
  "role": "string"       // 角色（USER/ADMIN，默认USER）
}
```

#### 响应示例
```json
{
  "code": 200,
  "msg": "注册成功",
  "data": {
    "id": 1,
    "username": "newuser",
    "role": "USER",
    "createdAt": "2023-01-01 12:00:00"
  }
}
```

---

## 2. 用户管理接口 (UserController)

### 2.1 获取用户列表
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/UserController.java`
- **请求路径**: `GET /api/users`
- **功能描述**: 获取所有用户信息列表
- **使用场景**: 
  - 前端用户管理页面 (`frontend/src/views/Users.vue`)
  - 管理员权限页面

#### 响应示例
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "username": "admin",
      "role": "ADMIN",
      "createdAt": "2023-01-01 12:00:00"
    }
  ]
}
```

### 2.2 添加用户
- **请求路径**: `POST /api/users`
- **功能描述**: 创建新用户

#### 请求参数
```json
{
  "username": "string",
  "password": "string",
  "role": "string"
}
```

### 2.3 更新用户
- **请求路径**: `PUT /api/users/{id}`
- **功能描述**: 更新指定用户信息

### 2.4 删除用户
- **请求路径**: `DELETE /api/users/{id}`
- **功能描述**: 删除指定用户

---

## 3. 监测点管理接口 (MonitoringPointController)

### 3.1 获取监测点列表
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/MonitoringPointController.java`
- **请求路径**: `GET /api/points`
- **功能描述**: 获取监测点信息，支持关键词搜索
- **使用场景**: 
  - 监测点管理页面 (`frontend/src/views/MonitoringPoints.vue`)
  - 预警列表页面监测点选择 (`frontend/src/views/Warnings.vue`)

#### 查询参数
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| keyword | string | 否 | 搜索关键词（可搜索名称、类型、位置、流域、传感器型号） |

#### 响应示例
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "长江监测点A",
      "type": "水位站",
      "location": "113.123,30.456",
      "riverBasin": "长江流域",
      "installHeight": 10.5,
      "sensorModel": "WL-2000",
      "createdAt": "2023-01-01 12:00:00"
    }
  ]
}
```

### 3.2 添加监测点
- **请求路径**: `POST /api/points`
- **功能描述**: 创建新监测点

#### 请求参数
```json
{
  "name": "string",           // 监测点名称
  "type": "string",           // 类型（雨量站/水位站等）
  "location": "string",       // 位置（经纬度或区域）
  "riverBasin": "string",     // 所属流域/河流
  "installHeight": "number",  // 安装高度（米）
  "sensorModel": "string"     // 传感器型号
}
```

### 3.3 更新监测点
- **请求路径**: `PUT /api/points/{id}`
- **功能描述**: 更新指定监测点信息

### 3.4 删除监测点
- **请求路径**: `DELETE /api/points/{id}`
- **功能描述**: 删除指定监测点及其关联的监测数据
- **注意事项**: 删除监测点时会级联删除该点的所有监测数据

---

## 4. 监测数据接口 (MonitorDataController)

### 4.1 获取监测数据
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/MonitorDataController.java`
- **请求路径**: `GET /api/monitor-data`
- **功能描述**: 获取指定监测点的历史数据
- **使用场景**: 
  - 历史数据查看页面 (`frontend/src/views/History.vue`)
  - 数据图表展示

#### 查询参数
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| pointId | Long | 是 | 监测点ID |
| start | DateTime | 否 | 开始时间 (yyyy-MM-dd HH:mm:ss) |
| end | DateTime | 否 | 结束时间 (yyyy-MM-dd HH:mm:ss) |

#### 响应示例
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "pointId": 1,
      "timestamp": "2023-01-01 12:00:00",
      "waterLevel": 5.2,      // 水位（米）
      "rainfall": 10.5,       // 降雨量（毫米）
      "flow": 120.8,          // 流量（立方米/秒）
      "otherParams": "{}"     // 其他参数（JSON字符串）
    }
  ]
}
```

### 4.2 批量删除监测数据
- **请求路径**: `DELETE /api/monitor-data/batch`
- **功能描述**: 批量删除指定监测点的所有数据
- **使用场景**: 数据管理，清理历史数据

#### 请求参数
```json
[1, 2, 3]  // 监测点ID数组
```

#### 响应示例
```json
{
  "code": 200,
  "data": {
    "totalDeleted": 150,
    "pointCounts": {
      "1": 50,
      "2": 60,
      "3": 40
    },
    "affectedPoints": 3
  }
}
```

### 4.3 获取监测点数据统计
- **请求路径**: `GET /api/monitor-data/count-by-points`
- **功能描述**: 统计指定监测点的数据量

#### 查询参数
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| pointIds | Array | 是 | 监测点ID数组 |

#### 响应示例
```json
{
  "code": 200,
  "data": {
    "pointCounts": {
      "1": 1500,
      "2": 2000
    },
    "totalCount": 3500
  }
}
```

---

## 5. 阈值配置接口 (ThresholdConfigController)

### 5.1 获取阈值配置
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/ThresholdConfigController.java`
- **请求路径**: `GET /api/thresholds`
- **功能描述**: 获取阈值配置信息
- **使用场景**: 阈值配置管理页面 (`frontend/src/views/Thresholds.vue`)

#### 查询参数
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| pointId | Long | 否 | 监测点ID |
| paramType | String | 否 | 参数类型（WATER_LEVEL/RAINFALL） |

#### 响应示例
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "pointId": 1,
      "level": 1,                 // 预警级别（1-4）
      "paramType": "WATER_LEVEL", // 参数类型
      "thresholdValue": 5.0,      // 阈值
      "duration": 30              // 持续时间（分钟）
    }
  ]
}
```

### 5.2 添加阈值配置
- **请求路径**: `POST /api/thresholds`
- **功能描述**: 创建新的阈值配置

#### 请求参数
```json
{
  "pointId": 1,
  "level": 1,
  "paramType": "WATER_LEVEL",
  "thresholdValue": 5.0,
  "duration": 30
}
```

### 5.3 更新阈值配置
- **请求路径**: `PUT /api/thresholds/{id}`
- **功能描述**: 更新指定阈值配置

### 5.4 删除阈值配置
- **请求路径**: `DELETE /api/thresholds/{id}`
- **功能描述**: 删除指定阈值配置

---

## 6. 预警事件接口 (WarningEventController)

### 6.1 获取预警事件列表
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/WarningEventController.java`
- **请求路径**: `GET /api/warnings`
- **功能描述**: 获取预警事件列表，返回包含监测点名称的增强信息
- **使用场景**: 
  - 预警列表页面 (`frontend/src/views/Warnings.vue`)
  - 实时预警监控

#### 查询参数
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| pointId | Long | 否 | 监测点ID |
| status | String | 否 | 状态过滤（未处理/已确认/已解除/ALL） |

#### 响应示例
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "pointId": 1,
      "pointName": "长江监测点A",
      "level": 2,
      "triggerValue": 6.5,
      "triggerTime": "2023-01-01 12:30:00",
      "status": "未处理",
      "handledBy": null
    }
  ]
}
```

### 6.2 确认预警事件
- **请求路径**: `PUT /api/warnings/{id}/confirm`
- **功能描述**: 确认预警事件，更改状态为"已确认"
- **使用场景**: 预警处理工作流

#### 查询参数
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| user | String | 否 | 处理人员 |

#### 响应示例
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "status": "已确认",
    "handledBy": "admin"
  }
}
```

### 6.3 解除预警事件
- **请求路径**: `PUT /api/warnings/{id}/resolve`
- **功能描述**: 解除预警事件，更改状态为"已解除"

### 6.4 删除预警事件
- **请求路径**: `DELETE /api/warnings/{id}`
- **功能描述**: 删除指定预警事件

---

## 7. 数据导入接口 (DataImportController)

### 7.1 监测数据导入
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/DataImportController.java`
- **请求路径**: `POST /api/monitor-data/import`
- **功能描述**: 批量导入监测数据，支持CSV和Excel格式
- **使用场景**: 数据导入页面 (`frontend/src/views/DataImport.vue`)

#### 请求参数
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| pointId | Long | 是 | 目标监测点ID |
| file | File | 是 | 数据文件(.csv/.xlsx) |

#### 文件格式要求

**CSV格式示例：**
```
timestamp,water_level,rainfall,flow
2023-01-01 12:00:00,5.2,10.5,120.8
2023-01-01 13:00:00,5.1,8.2,115.3
```

**Excel格式：**
- 第一行为标题行
- 列顺序：时间戳、水位、降雨量、流量
- 时间格式：yyyy-MM-dd HH:mm:ss

#### 响应示例
```json
{
  "code": 200,
  "data": {
    "total": 100,      // 总行数
    "success": 95,     // 成功导入数
    "failed": 5,       // 失败数
    "errors": [        // 错误信息（最多显示10条）
      "第3行解析失败: 时间格式错误",
      "第7行列数不足（至少3列）"
    ]
  }
}
```

#### 特殊功能
- **阈值评估**: 导入数据后自动进行阈值评估，可能触发预警事件
- **错误处理**: 详细记录导入过程中的错误，便于数据质量控制
- **格式兼容**: 支持CSV和Excel两种常用格式

---

## 8. 文件上传接口 (UploadController)

### 8.1 通用文件上传
- **接口位置**: `backend/src/main/java/com/yourcompany/yourapp/controller/UploadController.java`
- **请求路径**: `POST /api/upload`
- **功能描述**: 通用文件上传服务
- **使用场景**: 系统中需要文件上传的功能

#### 请求参数
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| file | File | 是 | 要上传的文件 |

#### 响应示例
```json
{
  "code": 200,
  "data": "/upload/abc123def456.jpg"  // 文件访问路径
}
```

#### 配置说明
- **上传目录**: 可通过 `app.upload-dir` 配置项设置，默认为 `./uploads`
- **文件命名**: 使用UUID生成唯一文件名，避免冲突
- **访问路径**: 返回相对路径，前端需要配合静态文件服务使用

---

## 数据模型说明

### 用户 (User)
```java
{
  "id": "Long",           // 主键ID
  "username": "String",   // 用户名
  "password": "String",   // 密码
  "role": "String",       // 角色（USER/ADMIN）
  "createdAt": "DateTime" // 创建时间
}
```

### 监测点 (MonitoringPoint)
```java
{
  "id": "Long",              // 主键ID
  "name": "String",          // 监测点名称
  "type": "String",          // 类型
  "location": "String",      // 位置
  "riverBasin": "String",    // 所属流域
  "installHeight": "Double", // 安装高度
  "sensorModel": "String",   // 传感器型号
  "createdAt": "DateTime"    // 创建时间
}
```

### 监测数据 (MonitorData)
```java
{
  "id": "Long",            // 主键ID
  "pointId": "Long",       // 监测点ID
  "timestamp": "DateTime", // 时间戳
  "waterLevel": "Double",  // 水位（米）
  "rainfall": "Double",    // 降雨量（毫米）
  "flow": "Double",        // 流量（立方米/秒）
  "otherParams": "String"  // 其他参数（JSON字符串）
}
```

### 预警事件 (WarningEvent)
```java
{
  "id": "Long",               // 主键ID
  "pointId": "Long",          // 监测点ID
  "level": "Integer",         // 预警级别（1-4）
  "triggerValue": "Double",   // 触发值
  "triggerTime": "DateTime",  // 触发时间
  "status": "String",         // 状态
  "handledBy": "String"       // 处理人员
}
```

### 阈值配置 (ThresholdConfig)
```java
{
  "id": "Long",                // 主键ID
  "pointId": "Long",           // 监测点ID
  "level": "Integer",          // 预警级别
  "paramType": "String",       // 参数类型
  "thresholdValue": "Double",  // 阈值
  "duration": "Integer"        // 持续时间（分钟）
}
```

---

## 前端路由与权限

### 路由配置
- **登录页面**: `/login` - 用户登录
- **注册页面**: `/register` - 用户注册
- **用户管理**: `/users` - 管理员专用
- **监测点管理**: `/points` - 管理员专用
- **数据导入**: `/import` - 管理员专用
- **阈值配置**: `/thresholds` - 管理员专用
- **预警列表**: `/warnings` - 通用页面
- **历史数据**: `/history` - 通用页面
- **GIS地图**: `/gis` - 通用页面
- **预测工作台**: `/forecast` - 通用页面

### 权限控制
- **管理员 (ADMIN)**: 拥有所有页面访问权限
- **普通用户 (USER)**: 仅可访问查看类页面，无法进行数据管理操作

---

## 错误处理

### 统一响应格式
```json
{
  "code": 200,     // 状态码：200成功，500错误
  "msg": "操作信息", // 操作结果描述
  "data": {}       // 响应数据
}
```

### 常见错误码
- **200**: 操作成功
- **500**: 服务器内部错误
- **401**: 未授权访问
- **403**: 权限不足
- **404**: 资源不存在

---

## 开发与部署说明

### 开发环境启动
1. **后端**: 运行 `Application.java` 主类，默认端口 8080
2. **前端**: 执行 `npm run dev`，默认端口 5173
3. **数据库**: 确保 MySQL 服务运行，导入 `database/soft_db_cleaned.sql`

### API 测试
可以使用 Postman 或类似工具测试API接口，注意：
- 所有接口均以 `/api` 为前缀
- 需要登录的接口需要在 Header 中添加 Authorization 字段
- 文件上传接口使用 `multipart/form-data` 格式

### 数据库表结构
主要数据表：
- `sys_user`: 系统用户表
- `monitoring_point`: 监测点表
- `monitor_data`: 监测数据表
- `warning_event`: 预警事件表
- `threshold_config`: 阈值配置表

---

## 扩展说明

该系统为洪水监测基础框架，支持以下扩展：

1. **预测功能**: 已预留 `PredictRequest` DTO 和相关接口，可集成机器学习模型
2. **GIS功能**: 前端已有 GIS 页面，可集成地图服务
3. **实时推送**: 可集成 WebSocket 实现实时预警推送
4. **数据可视化**: 可扩展图表展示功能
5. **移动端**: RESTful API 设计便于移动端集成

该文档涵盖了系统的所有主要API接口，为后续开发和维护提供完整的参考。