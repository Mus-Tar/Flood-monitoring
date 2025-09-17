package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("warning_event")
public class WarningEvent {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long pointId;      // DB: point_id
    private Integer level;     // 触发级别
    private Double triggerValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime triggerTime;

    private String status;     // 未处理 / 已确认 / 已解除
    private String handledBy;
}
