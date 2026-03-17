package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("warning_event")
// 预警事件记录实体
public class WarningEvent {

    @TableId(type = IdType.AUTO)
    private Long id;                 // 主键ID

    private Long pointId;            // 监测点ID
    private Integer level;           // 触发的预警等级（预警级别）
    private Double triggerValue;     // 实际触发数值

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime triggerTime; // 触发时间

    private String status;           // 处理状态
    private String handledBy;        // 处理人
}
