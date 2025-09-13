package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("threshold_config")
public class ThresholdConfig {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long pointId;         // DB: point_id
    private Integer level;        // 1-4
    private String paramType;     // WATER_LEVEL / RAINFALL
    private Double thresholdValue;// DB: threshold_value
    private Integer duration;     // 持续分钟
}
