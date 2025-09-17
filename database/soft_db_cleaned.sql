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
('admin', 'admin123', 'ADMIN') 
ON DUPLICATE KEY UPDATE username=username;

-- 插入示例监测点数据
INSERT INTO monitoring_point (name, type, location, river_basin) VALUES 
('长江水位站001', 'WATER_LEVEL', '经度:118.123, 纬度:32.456', '长江流域'),
('雨量监测站002', 'RAINFALL', '经度:118.234, 纬度:32.567', '长江流域'),
('综合监测点003', 'WATER_LEVEL', '经度:118.345, 纬度:32.678', '长江流域')
ON DUPLICATE KEY UPDATE name=name;

-- 插入示例阈值配置
INSERT INTO threshold_config (point_id, level, param_type, threshold_value, duration) VALUES 
(1, 1, 'water_level', 3.5, 30),
(1, 2, 'water_level', 4.0, 30),
(1, 3, 'water_level', 4.5, 15),
(1, 4, 'water_level', 5.0, 10),
(2, 1, 'rainfall', 50.0, 60),
(2, 2, 'rainfall', 80.0, 60),
(2, 3, 'rainfall', 120.0, 30),
(2, 4, 'rainfall', 150.0, 30)
ON DUPLICATE KEY UPDATE threshold_value=threshold_value;

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

已删除的无用表：
- action_log (预警处置日志表)
- data_quality_issue (数据质量问题记录表)
- emergency_plan (应急预案表)  
- plan_region (预案与监测点关联表)
- rescue_resource (救援物资表)
- rescue_team (救援队伍表)
- risk_assessment (风险评估表)
- model_info (模型配置表)
- forecast (预测结果表)

这个精简版本专注于核心的监测、预警和数据管理功能，
去除了未使用的复杂业务逻辑，便于维护和部署。
*/