package com.yourcompany.yourapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PredictRequest {

    // 监测点 ID
    private Long pointId;

    // 预测参数类型（如 WATER_LEVEL、RAINFALL）
    private String param;

    // 模型类型标识（支持 BASELINE / 预留 LSTM、RF 扩展）
    private String modelType;

    // 指定具体模型 ID（可选）
    private Long modelId;

    // 预测时长（小时），用于确定预测时间范围
    private Integer horizonHours;

    // 时间步长（分钟）
    private Integer stepMinutes;

    // 预测起始时间（为空时使用系统默认时间）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    // 是否保存预测结果（用于演示或后续扩展）
    private Boolean save;
}
