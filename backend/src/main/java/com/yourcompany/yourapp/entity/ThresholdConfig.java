package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("threshold_config")
// 监测参数阈值配置实体
public class ThresholdConfig {

    @TableId(type = IdType.AUTO)
    private Long id;                 // 主键ID

    private Long pointId;            // 监测点ID
    private Integer level;           // 预警等级（1-4）
    private String paramType;        // 参数类型（水位/降雨）
    private Double thresholdValue;   // 阈值数值
    private Integer duration;        // 持续时间（分钟）
}
