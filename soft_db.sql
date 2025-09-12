SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS sys_user (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    role VARCHAR(50) DEFAULT 'user' COMMENT '角色（admin/user/guest等）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT='系统用户表';

-- 创建监测点表
CREATE TABLE monitoring_point (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '监测点ID',
    name VARCHAR(100) NOT NULL COMMENT '监测点名称',
    type VARCHAR(50) NOT NULL COMMENT '类型（如雨量站、水位站等）',
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
    param_type VARCHAR(50) NOT NULL COMMENT '参数类型（如水位、雨量）',
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
    plan_id INT COMMENT '关联应急预案ID',
    FOREIGN KEY (point_id) REFERENCES monitoring_point(id)
) COMMENT='预警事件记录表';

-- 创建数据质量问题记录表
CREATE TABLE data_quality_issue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '质量问题ID',
    point_id INT NOT NULL COMMENT '监测点ID',
    timestamp DATETIME NOT NULL COMMENT '问题时间',
    issue_type VARCHAR(100) NOT NULL COMMENT '问题类型（缺测、异常高值等）',
    description TEXT COMMENT '详细描述',
    FOREIGN KEY (point_id) REFERENCES monitoring_point(id)
) COMMENT='数据质量问题记录';

-- 创建模型配置/结果表
CREATE TABLE model_info (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '模型ID',
    model_type VARCHAR(50) NOT NULL COMMENT '模型类型（如LSTM、RF等）',
    target VARCHAR(50) NOT NULL COMMENT '预测目标（如水位）',
    train_start DATE COMMENT '训练起始日期',
    train_end DATE COMMENT '训练结束日期',
    parameters JSON COMMENT '模型参数（JSON格式）',
    status VARCHAR(50) DEFAULT '训练中' COMMENT '训练状态',
    model_path VARCHAR(255) COMMENT '模型文件路径',
    eval_metrics JSON COMMENT '评估指标（如准确率、误差等）'
) COMMENT='模型配置及结果表';

-- 创建预测结果表
CREATE TABLE forecast (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '预测结果ID',
    point_id INT NOT NULL COMMENT '监测点ID',
    forecast_time DATETIME NOT NULL COMMENT '预测目标时间',
    param VARCHAR(50) NOT NULL COMMENT '预测参数（如水位/雨量）',
    predicted_value DOUBLE NOT NULL COMMENT '预测值',
    model_id INT COMMENT '模型ID',
    generate_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预测生成时间',
    FOREIGN KEY (point_id) REFERENCES monitoring_point(id),
    FOREIGN KEY (model_id) REFERENCES model_info(id)
) COMMENT='模型预测结果表';

-- 创建风险评估表
CREATE TABLE risk_assessment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '风险评估ID',
    point_id INT COMMENT '站点ID',
    region VARCHAR(100) COMMENT '区域名称（如有）',
    risk_level VARCHAR(20) COMMENT '风险等级（如高、中、低）',
    assessment_time DATETIME NOT NULL COMMENT '评估时间',
    details TEXT COMMENT '风险描述及原因'
) COMMENT='风险评估结果表';

-- 创建应急预案表
CREATE TABLE emergency_plan (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '预案ID',
    name VARCHAR(100) NOT NULL COMMENT '预案名称',
    trigger_level INT NOT NULL COMMENT '触发等级',
    conditions TEXT COMMENT '触发条件描述',
    procedures TEXT COMMENT '处置步骤',
    resources TEXT COMMENT '所需资源描述',
    last_update DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间'
) COMMENT='应急预案表';

-- 创建预案与监测点关联表
CREATE TABLE plan_region (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT NOT NULL,
    point_id INT COMMENT '监测点ID',
    region_code VARCHAR(50) COMMENT '区域代码',
    FOREIGN KEY (plan_id) REFERENCES emergency_plan(id)
) COMMENT='应急预案与区域/监测点关联表';

-- 创建救援队伍表
CREATE TABLE rescue_team (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '队伍ID',
    name VARCHAR(100) NOT NULL COMMENT '队伍名称',
    type VARCHAR(50) COMMENT '队伍类型（如消防、医疗）',
    member_count INT COMMENT '队伍人数',
    skills TEXT COMMENT '专业技能',
    contact VARCHAR(100) COMMENT '联系方式',
    current_location VARCHAR(100) COMMENT '当前驻地',
    status VARCHAR(50) DEFAULT '待命' COMMENT '状态'
) COMMENT='救援队伍信息表';

-- 创建救援物资表
CREATE TABLE rescue_resource (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '物资ID',
    name VARCHAR(100) NOT NULL COMMENT '物资名称',
    category VARCHAR(50) COMMENT '类别（装备、耗材等）',
    quantity INT DEFAULT 0 COMMENT '库存数量',
    location VARCHAR(100) COMMENT '存放位置',
    unit VARCHAR(20) COMMENT '单位',
    threshold INT DEFAULT 0 COMMENT '补货阈值',
    status VARCHAR(50) DEFAULT '可用' COMMENT '状态'
) COMMENT='救援物资表';

-- 创建预警处置日志表
CREATE TABLE action_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    event_id BIGINT NOT NULL COMMENT '关联预警事件ID',
    timestamp DATETIME NOT NULL COMMENT '操作时间',
    user VARCHAR(100) COMMENT '操作人',
    action VARCHAR(100) COMMENT '操作类型',
    detail TEXT COMMENT '详细描述',
    FOREIGN KEY (event_id) REFERENCES warning_event(id)
) COMMENT='预警处置日志表';

 