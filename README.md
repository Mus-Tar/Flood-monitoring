# Flood monitoring （半分离演示模板）

技术栈：Spring Boot 2.7.x + MyBatis-Plus + MySQL 8 + Vue3 + Vite + TailwindCSS

## 目录结构
```
Flood monitoring/
├─ backend/   # 后端模块（Spring Boot）
└─ frontend/  # 前端模块（Vite + Vue3）
```

## 准备数据库
1. 创建数据库 `soft_db`
2. 导入：`backend/src/main/resources/schema.sql`

## 启动后端
```bash
cd backend
# 用 IDE 运行 Application.java，或
./mvnw spring-boot:run   # 若无 mvnw，可先 mvn -v 确认 Maven 环境
```
默认端口：8080（见 `application.yml`）

## 启动前端（开发）
```bash
cd frontend
npm i
npm run dev
```
访问：http://localhost:3000

## 打包前端并嵌入后端
```bash
cd frontend
npm run build
```
构建产物输出到：`backend/src/main/resources/static`，后端打包后可直接访问 SPA。

## API 约定
- 统一前缀：`/api`
- 统一响应：`{ code, msg, data }`

### 示例接口
- 登录：`POST /api/auth/login`  body: `{ username, password }`（演示账号：admin/admin123）
- 用户列表：`GET /api/users`
- 新增用户：`POST /api/users`
- 修改用户：`PUT /api/users/{id}`
- 删除用户：`DELETE /api/users/{id}`
- 文件上传：`POST /api/upload`   form-data: `file`  返回 `/upload/{filename}`

## 上传目录
- 默认可写目录：`./uploads`（配置：`app.upload-dir`）
- 额外映射：`classpath:/static.upload/`（仅为演示保留）

## 注意
- 生产不要把上传目录放在 classpath 内；示例已将可写目录设置为 `./uploads`。
- 若端口/数据库配置不同，请修改 `backend/src/main/resources/application.yml`。
- 本模板用于软件著作权演示，未集成权限/鉴权与严格安全校验。
