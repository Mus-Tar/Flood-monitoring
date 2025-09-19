SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_general_ci;

-- =============================================
-- 洪水监测系统数据库结构（精简版）
-- 创建日期: 2025-09-17
-- 说明: 只包含项目中实际使用的数据表
-- =============================================

-- 创建系统用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    role VARCHAR(50) DEFAULT 'USER' COMMENT '角色（ADMIN/USER）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT='系统用户表';

-- 创建监测点表
CREATE TABLE monitoring_point (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '监测点ID',
    name VARCHAR(100) NOT NULL COMMENT '监测点名称',
    type VARCHAR(50) NOT NULL COMMENT '类型（WATER_LEVEL/RAINFALL）',
    location VARCHAR(255) COMMENT '位置（经纬度或区域）',
    river_basin VARCHAR(100) COMMENT '所属流域/河流',
    install_height DOUBLE COMMENT '安装高度（单位：米）',
    sensor_model VARCHAR(100) COMMENT '传感器型号',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='监测点信息表';

-- 创建监测数据表
CREATE TABLE monitor_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '数据ID',
    point_id INT NOT NULL COMMENT '监测点ID',
    timestamp DATETIME NOT NULL COMMENT '数据时间戳',
    water_level DOUBLE COMMENT '水位（米）',
    rainfall DOUBLE COMMENT '降雨量（毫米）',
    flow DOUBLE COMMENT '流量（可选，立方米/秒）',
    other_params JSON COMMENT '其他参数（可扩展）',
    FOREIGN KEY (point_id) REFERENCES monitoring_point(id)
) COMMENT='监测数据表';

-- 创建预警阈值配置表
CREATE TABLE threshold_config (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '阈值配置ID',
    point_id INT NOT NULL COMMENT '关联监测点',
    level INT NOT NULL COMMENT '预警级别（1-4）',
    param_type VARCHAR(50) NOT NULL COMMENT '参数类型（如water_level、rainfall）',
    threshold_value DOUBLE NOT NULL COMMENT '阈值数值',
    duration INT DEFAULT 0 COMMENT '持续时间（分钟）',
    FOREIGN KEY (point_id) REFERENCES monitoring_point(id)
) COMMENT='预警阈值配置表';

-- 创建预警事件表
CREATE TABLE warning_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '预警事件ID',
    point_id INT NOT NULL COMMENT '监测点ID',
    level INT NOT NULL COMMENT '触发等级',
    trigger_value DOUBLE NOT NULL COMMENT '触发时数值',
    trigger_time DATETIME NOT NULL COMMENT '触发时间',
    status VARCHAR(50) DEFAULT '未处理' COMMENT '状态（未处理、已确认、已解除等）',
    handled_by VARCHAR(100) COMMENT '处理人',
    FOREIGN KEY (point_id) REFERENCES monitoring_point(id)
) COMMENT='预警事件记录表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入默认管理员账户
INSERT INTO sys_user (username, password, role) VALUES 
('admin', 'admin123', 'ADMIN'),
('user', 'user123', 'USER') 
ON DUPLICATE KEY UPDATE username=username;

-- 插入示例监测点数据
INSERT INTO monitoring_point (id, name, type, location, river_basin, install_height, sensor_model, created_at) VALUES 
(1, '珠江北1号', 'WATER_LEVEL', '23.108944,113.341791', '珠江', 1.6, 'v1.0', '2025-09-15 14:11:12'),
(2, '珠江北2号', 'WATER_LEVEL', '23.107997,113.379491', '珠江', 1.6, 'v1.0', '2025-09-15 14:11:27'),
(3, '珠江北3号', 'WATER_LEVEL', '23.094852,113.404002', '珠江', 1.6, 'v1.0', '2025-09-15 14:12:01'),
(4, '黄埔水号', 'WATER_LEVEL', '23.094181,113.438032', '黄埔水', 1.3, 'v2.0', '2025-09-15 14:12:26'),
(5, '仓头海号', 'WATER_LEVEL', '23.075942,113.375613', '仓头海', 1.3, 'v2.0', '2025-09-15 14:12:42'),
(6, '珠江南号', 'WATER_LEVEL', '23.045932,113.337659', '珠江', 1.6, 'v1.0', '2025-09-15 14:13:10'),
(7, '海心岗号', 'WATER_LEVEL', '23.034559,113.369601', '海心岗', 1.5, 'v2.0', '2025-09-15 14:13:33'),
(8, '沥滘水1号', 'WATER_LEVEL', '23.036138,113.404978', '沥滘水', 1.2, 'v1.0', '2025-09-15 14:15:30'),
(9, '沥滘水2号', 'WATER_LEVEL', '23.068519,113.428161', '沥滘水', 1.2, 'v1.0', '2025-09-15 14:15:36')
ON DUPLICATE KEY UPDATE 
  name=VALUES(name), 
  type=VALUES(type), 
  location=VALUES(location), 
  river_basin=VALUES(river_basin), 
  install_height=VALUES(install_height), 
  sensor_model=VALUES(sensor_model), 
  created_at=VALUES(created_at);

-- 插入示例阈值配置
INSERT INTO threshold_config (id, point_id, level, param_type, threshold_value, duration) VALUES 
(1, 1, 4, 'WATER_LEVEL', 1.9, 1),
(2, 2, 1, 'WATER_LEVEL', 1.8, 0),
(3, 3, 3, 'WATER_LEVEL', 1.9, 0),
(4, 4, 2, 'WATER_LEVEL', 1.8, 0),
(5, 5, 3, 'WATER_LEVEL', 1.9, 0),
(6, 6, 2, 'WATER_LEVEL', 2.0, 0),
(7, 7, 1, 'WATER_LEVEL', 1.9, 0),
(8, 8, 2, 'WATER_LEVEL', 1.8, 0),
(9, 9, 1, 'WATER_LEVEL', 1.7, 0)
ON DUPLICATE KEY UPDATE 
  point_id=VALUES(point_id), 
  level=VALUES(level), 
  param_type=VALUES(param_type), 
  threshold_value=VALUES(threshold_value), 
  duration=VALUES(duration);

-- =============================================
-- 索引优化
-- =============================================

-- 监测数据表索引（提高查询性能）
CREATE INDEX idx_monitor_data_point_time ON monitor_data(point_id, timestamp);
CREATE INDEX idx_monitor_data_timestamp ON monitor_data(timestamp);

-- 预警事件表索引
CREATE INDEX idx_warning_event_point_time ON warning_event(point_id, trigger_time);
CREATE INDEX idx_warning_event_status ON warning_event(status);

-- 阈值配置表索引
CREATE INDEX idx_threshold_config_point ON threshold_config(point_id);

-- =============================================
-- 表结构说明
-- =============================================

/*
精简后的数据库包含5个核心表：

1. sys_user - 系统用户表
   - 支持ADMIN/USER两种角色
   - 用于用户认证和权限控制

2. monitoring_point - 监测点表
   - 存储监测点基本信息
   - 支持WATER_LEVEL/RAINFALL两种类型

3. monitor_data - 监测数据表  
   - 存储实时监测数据
   - 支持水位、雨量、流量等多种参数

4. threshold_config - 预警阈值配置表
   - 配置各级别预警阈值
   - 支持不同参数类型的阈值设置

5. warning_event - 预警事件表
   - 记录触发的预警事件
   - 支持事件状态管理（已移除plan_id字段）

这个精简版本专注于核心的监测、预警和数据管理功能，
去除了未使用的复杂业务逻辑，便于维护和部署。
*/