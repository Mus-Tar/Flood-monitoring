package com.yourcompany.yourapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarningEventVO {

    // 预警事件主键
    private Long id;

    // 关联监测点 ID
    private Long pointId;

    // 监测点名称
    private String pointName;

    // 预警等级
    private Integer level;

    // 触发预警的阈值
    private Double triggerValue;

    // 预警触发时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime triggerTime;

    // 预警处理状态
    private String status;

    // 处理人标识
    private String handledBy;
}
