package com.yourcompany.yourapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarningEventVO {
    private Long id;
    private Long pointId;
    private String pointName;
    private Integer level;
    private Double triggerValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime triggerTime;
    private String status;
    private String handledBy;
}
