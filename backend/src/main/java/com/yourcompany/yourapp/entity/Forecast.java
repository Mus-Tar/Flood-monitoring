package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("forecast")
public class Forecast {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long pointId;                 // point_id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime forecastTime;   // 预测目标时间
    private String param;                 // WATER_LEVEL / RAINFALL
    private Double predictedValue;
    private Long modelId;                 // 采用的模型ID（可空）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generateTime;   // 生成时间
}
