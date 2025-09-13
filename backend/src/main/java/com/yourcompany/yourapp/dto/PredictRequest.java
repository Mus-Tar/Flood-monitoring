package com.yourcompany.yourapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PredictRequest {
    private Long pointId;            // required
    private String param;            // WATER_LEVEL / RAINFALL
    private String modelType;        // LSTM / RF / BASELINE（默认 BASELINE）
    private Long modelId;            // 可选，指定具体模型

    private Integer horizonHours;    // 预测时长（小时），默认 24
    private Integer stepMinutes;     // 步长（分钟），默认 60

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime; // 可选，默认 now 的下一个步长
    private Boolean save;            // 是否写入 forecast 表，默认 true
}
