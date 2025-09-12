package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("monitor_data")
public class MonitorData {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long pointId; // 监测点ID（DB列：point_id）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private Double waterLevel;   // 水位（米）
    private Double rainfall;     // 降雨量（毫米）
    private Double flow;         // 流量（可选，m^3/s）

    /** 其他参数（DB是 JSON，我们这先用String接，前端用不上可以先忽略） */
    private String otherParams;
}
