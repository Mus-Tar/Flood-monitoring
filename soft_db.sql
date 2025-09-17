/*
 Navicat Premium Dump SQL

 Source Server         : mustar
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : soft_db

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 17/09/2025 10:44:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for action_log
-- ----------------------------
DROP TABLE IF EXISTS `action_log`;
CREATE TABLE `action_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `event_id` bigint NOT NULL COMMENT '关联预警事件ID',
  `timestamp` datetime NOT NULL COMMENT '操作时间',
  `user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `action` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详细描述',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `event_id`(`event_id` ASC) USING BTREE,
  CONSTRAINT `action_log_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `warning_event` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预警处置日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of action_log
-- ----------------------------

-- ----------------------------
-- Table structure for data_quality_issue
-- ----------------------------
DROP TABLE IF EXISTS `data_quality_issue`;
CREATE TABLE `data_quality_issue`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '质量问题ID',
  `point_id` int NOT NULL COMMENT '监测点ID',
  `timestamp` datetime NOT NULL COMMENT '问题时间',
  `issue_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题类型（缺测、异常高值等）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详细描述',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `point_id`(`point_id` ASC) USING BTREE,
  CONSTRAINT `data_quality_issue_ibfk_1` FOREIGN KEY (`point_id`) REFERENCES `monitoring_point` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据质量问题记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_quality_issue
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_plan
-- ----------------------------
DROP TABLE IF EXISTS `emergency_plan`;
CREATE TABLE `emergency_plan`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '预案ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预案名称',
  `trigger_level` int NOT NULL COMMENT '触发等级',
  `conditions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '触发条件描述',
  `procedures` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '处置步骤',
  `resources` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所需资源描述',
  `last_update` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急预案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of emergency_plan
-- ----------------------------

-- ----------------------------
-- Table structure for forecast
-- ----------------------------
DROP TABLE IF EXISTS `forecast`;
CREATE TABLE `forecast`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预测结果ID',
  `point_id` int NOT NULL COMMENT '监测点ID',
  `forecast_time` datetime NOT NULL COMMENT '预测目标时间',
  `param` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预测参数（如水位/雨量）',
  `predicted_value` double NOT NULL COMMENT '预测值',
  `model_id` int NULL DEFAULT NULL COMMENT '模型ID',
  `generate_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预测生成时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `point_id`(`point_id` ASC) USING BTREE,
  INDEX `model_id`(`model_id` ASC) USING BTREE,
  CONSTRAINT `forecast_ibfk_1` FOREIGN KEY (`point_id`) REFERENCES `monitoring_point` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `forecast_ibfk_2` FOREIGN KEY (`model_id`) REFERENCES `model_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模型预测结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forecast
-- ----------------------------

-- ----------------------------
-- Table structure for model_info
-- ----------------------------
DROP TABLE IF EXISTS `model_info`;
CREATE TABLE `model_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '模型ID',
  `model_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型类型（如LSTM、RF等）',
  `target` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预测目标（如水位）',
  `train_start` date NULL DEFAULT NULL COMMENT '训练起始日期',
  `train_end` date NULL DEFAULT NULL COMMENT '训练结束日期',
  `parameters` json NULL COMMENT '模型参数（JSON格式）',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '训练中' COMMENT '训练状态',
  `model_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型文件路径',
  `eval_metrics` json NULL COMMENT '评估指标（如准确率、误差等）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模型配置及结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of model_info
-- ----------------------------

-- ----------------------------
-- Table structure for monitor_data
-- ----------------------------
DROP TABLE IF EXISTS `monitor_data`;
CREATE TABLE `monitor_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `point_id` int NOT NULL COMMENT '监测点ID',
  `timestamp` datetime NOT NULL COMMENT '数据时间戳',
  `water_level` double NULL DEFAULT NULL COMMENT '水位（米）',
  `rainfall` double NULL DEFAULT NULL COMMENT '降雨量（毫米）',
  `flow` double NULL DEFAULT NULL COMMENT '流量（可选，立方米/秒）',
  `other_params` json NULL COMMENT '其他参数（可扩展）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `point_id`(`point_id` ASC) USING BTREE,
  CONSTRAINT `monitor_data_ibfk_1` FOREIGN KEY (`point_id`) REFERENCES `monitoring_point` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6807 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '监测数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of monitor_data
-- ----------------------------
INSERT INTO `monitor_data` VALUES (111, 5, '2025-09-01 00:00:00', 1.58, 0, 0.74, NULL);
INSERT INTO `monitor_data` VALUES (112, 5, '2025-09-01 01:00:00', 1.67, 0, 0.74, NULL);
INSERT INTO `monitor_data` VALUES (113, 5, '2025-09-01 02:00:00', 1.8, 0, 0.79, NULL);
INSERT INTO `monitor_data` VALUES (114, 5, '2025-09-01 03:00:00', 1.74, 0, 0.76, NULL);
INSERT INTO `monitor_data` VALUES (115, 5, '2025-09-01 04:00:00', 1.76, 0, 0.74, NULL);
INSERT INTO `monitor_data` VALUES (116, 5, '2025-09-01 05:00:00', 1.66, 0, 0.75, NULL);
INSERT INTO `monitor_data` VALUES (117, 5, '2025-09-01 06:00:00', 1.58, 0, 0.73, NULL);
INSERT INTO `monitor_data` VALUES (118, 5, '2025-09-01 07:00:00', 1.45, 0, 0.75, NULL);
INSERT INTO `monitor_data` VALUES (119, 5, '2025-09-01 08:00:00', 1.44, 0, 0.7, NULL);
INSERT INTO `monitor_data` VALUES (120, 5, '2025-09-01 09:00:00', 1.37, 0, 0.71, NULL);
INSERT INTO `monitor_data` VALUES (121, 5, '2025-09-01 10:00:00', 1.33, 0, 0.69, NULL);
INSERT INTO `monitor_data` VALUES (122, 5, '2025-09-01 11:00:00', 1.41, 0, 0.68, NULL);
INSERT INTO `monitor_data` VALUES (123, 5, '2025-09-01 12:00:00', 1.52, 0, 0.71, NULL);
INSERT INTO `monitor_data` VALUES (124, 5, '2025-09-01 13:00:00', 1.57, 0, 0.75, NULL);
INSERT INTO `monitor_data` VALUES (125, 5, '2025-09-01 14:00:00', 1.65, 0, 0.76, NULL);
INSERT INTO `monitor_data` VALUES (126, 5, '2025-09-01 15:00:00', 1.62, 0, 0.73, NULL);
INSERT INTO `monitor_data` VALUES (127, 5, '2025-09-01 16:00:00', 1.64, 0, 0.75, NULL);
INSERT INTO `monitor_data` VALUES (128, 5, '2025-09-01 17:00:00', 1.57, 0, 0.77, NULL);
INSERT INTO `monitor_data` VALUES (129, 5, '2025-09-01 18:00:00', 1.46, 0, 0.72, NULL);
INSERT INTO `monitor_data` VALUES (130, 5, '2025-09-01 19:00:00', 1.43, 0, 0.71, NULL);
INSERT INTO `monitor_data` VALUES (131, 5, '2025-09-01 20:00:00', 1.38, 0, 0.66, NULL);
INSERT INTO `monitor_data` VALUES (132, 5, '2025-09-01 21:00:00', 1.35, 0, 0.69, NULL);
INSERT INTO `monitor_data` VALUES (133, 5, '2025-09-01 22:00:00', 1.4, 0, 0.72, NULL);
INSERT INTO `monitor_data` VALUES (134, 5, '2025-09-01 23:00:00', 1.47, 0, 0.73, NULL);


-- ----------------------------
-- Table structure for monitoring_point
-- ----------------------------
DROP TABLE IF EXISTS `monitoring_point`;
CREATE TABLE `monitoring_point`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '监测点ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '监测点名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（如雨量站、水位站等）',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置（经纬度或区域）',
  `river_basin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属流域/河流',
  `install_height` double NULL DEFAULT NULL COMMENT '安装高度（单位：米）',
  `sensor_model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '传感器型号',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '监测点信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of monitoring_point
-- ----------------------------
INSERT INTO `monitoring_point` VALUES (5, '珠江北1号', 'WATER_LEVEL', '23.108944,113.341791', NULL, NULL, NULL, '2025-09-15 14:11:12');
INSERT INTO `monitoring_point` VALUES (6, '珠江北2号', 'WATER_LEVEL', '23.107997,113.379491', NULL, NULL, NULL, '2025-09-15 14:11:27');
INSERT INTO `monitoring_point` VALUES (7, '珠江北3号', 'WATER_LEVEL', '23.094852,113.404002', NULL, NULL, NULL, '2025-09-15 14:12:01');
INSERT INTO `monitoring_point` VALUES (8, '黄埔水号', 'WATER_LEVEL', '23.094181,113.438032', NULL, NULL, NULL, '2025-09-15 14:12:26');
INSERT INTO `monitoring_point` VALUES (9, '仓头海号', 'WATER_LEVEL', '23.075942,113.375613', NULL, NULL, NULL, '2025-09-15 14:12:42');
INSERT INTO `monitoring_point` VALUES (10, '珠江南号', 'WATER_LEVEL', '23.045932,113.337659', NULL, NULL, NULL, '2025-09-15 14:13:10');
INSERT INTO `monitoring_point` VALUES (11, '海心岗号', 'WATER_LEVEL', '23.034559,113.369601', NULL, NULL, NULL, '2025-09-15 14:13:33');
INSERT INTO `monitoring_point` VALUES (13, '沥滘水1号', 'WATER_LEVEL', '23.036138,113.404978', NULL, NULL, NULL, '2025-09-15 14:15:30');
INSERT INTO `monitoring_point` VALUES (14, '沥滘水2号', 'WATER_LEVEL', '23.068519,113.428161', NULL, NULL, NULL, '2025-09-15 14:15:36');

-- ----------------------------
-- Table structure for plan_region
-- ----------------------------
DROP TABLE IF EXISTS `plan_region`;
CREATE TABLE `plan_region`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_id` int NOT NULL,
  `point_id` int NULL DEFAULT NULL COMMENT '监测点ID',
  `region_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域代码',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `plan_id`(`plan_id` ASC) USING BTREE,
  CONSTRAINT `plan_region_ibfk_1` FOREIGN KEY (`plan_id`) REFERENCES `emergency_plan` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急预案与区域/监测点关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of plan_region
-- ----------------------------

-- ----------------------------
-- Table structure for rescue_resource
-- ----------------------------
DROP TABLE IF EXISTS `rescue_resource`;
CREATE TABLE `rescue_resource`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '物资ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物资名称',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类别（装备、耗材等）',
  `quantity` int NULL DEFAULT 0 COMMENT '库存数量',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存放位置',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `threshold` int NULL DEFAULT 0 COMMENT '补货阈值',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '可用' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '救援物资表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rescue_resource
-- ----------------------------

-- ----------------------------
-- Table structure for rescue_team
-- ----------------------------
DROP TABLE IF EXISTS `rescue_team`;
CREATE TABLE `rescue_team`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '队伍ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '队伍名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '队伍类型（如消防、医疗）',
  `member_count` int NULL DEFAULT NULL COMMENT '队伍人数',
  `skills` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '专业技能',
  `contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `current_location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前驻地',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '待命' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '救援队伍信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rescue_team
-- ----------------------------

-- ----------------------------
-- Table structure for risk_assessment
-- ----------------------------
DROP TABLE IF EXISTS `risk_assessment`;
CREATE TABLE `risk_assessment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '风险评估ID',
  `point_id` int NULL DEFAULT NULL COMMENT '站点ID',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称（如有）',
  `risk_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '风险等级（如高、中、低）',
  `assessment_time` datetime NOT NULL COMMENT '评估时间',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '风险描述及原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '风险评估结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of risk_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码（加密存储）',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'user' COMMENT '角色（admin/user/guest等）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (4, 'mustar', '', 'ADMIN', '2025-09-15 14:09:43');
INSERT INTO `sys_user` VALUES (7, '123456', '123456', 'ADMIN', '2025-09-15 14:09:43');

-- ----------------------------
-- Table structure for threshold_config
-- ----------------------------
DROP TABLE IF EXISTS `threshold_config`;
CREATE TABLE `threshold_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '阈值配置ID',
  `point_id` int NOT NULL COMMENT '关联监测点',
  `level` int NOT NULL COMMENT '预警级别（1-4）',
  `param_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数类型（如水位、雨量）',
  `threshold_value` double NOT NULL COMMENT '阈值数值',
  `duration` int NULL DEFAULT 0 COMMENT '持续时间（分钟）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `point_id`(`point_id` ASC) USING BTREE,
  CONSTRAINT `threshold_config_ibfk_1` FOREIGN KEY (`point_id`) REFERENCES `monitoring_point` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预警阈值配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of threshold_config
-- ----------------------------
INSERT INTO `threshold_config` VALUES (6, 14, 1, 'WATER_LEVEL', 1.7, 0);
INSERT INTO `threshold_config` VALUES (7, 13, 2, 'WATER_LEVEL', 1.8, 0);
INSERT INTO `threshold_config` VALUES (8, 13, 1, 'WATER_LEVEL', 1.9, 0);
INSERT INTO `threshold_config` VALUES (9, 10, 2, 'WATER_LEVEL', 2, 0);
INSERT INTO `threshold_config` VALUES (10, 9, 3, 'WATER_LEVEL', 1.9, 0);
INSERT INTO `threshold_config` VALUES (11, 8, 2, 'WATER_LEVEL', 1.8, 0);
INSERT INTO `threshold_config` VALUES (12, 7, 3, 'WATER_LEVEL', 1.9, 0);
INSERT INTO `threshold_config` VALUES (13, 6, 1, 'WATER_LEVEL', 1.8, 0);
INSERT INTO `threshold_config` VALUES (14, 5, 4, 'WATER_LEVEL', 1.9, 0);

-- ----------------------------
-- Table structure for warning_event
-- ----------------------------
DROP TABLE IF EXISTS `warning_event`;
CREATE TABLE `warning_event`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警事件ID',
  `point_id` int NOT NULL COMMENT '监测点ID',
  `level` int NOT NULL COMMENT '触发等级',
  `trigger_value` double NOT NULL COMMENT '触发时数值',
  `trigger_time` datetime NOT NULL COMMENT '触发时间',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '未处理' COMMENT '状态（未处理、已确认、已解除等）',
  `handled_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人',
  `plan_id` int NULL DEFAULT NULL COMMENT '关联应急预案ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `point_id`(`point_id` ASC) USING BTREE,
  CONSTRAINT `warning_event_ibfk_1` FOREIGN KEY (`point_id`) REFERENCES `monitoring_point` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预警事件记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of warning_event
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
