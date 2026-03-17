package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("monitor_data")
// 监测点采集数据实体
public class MonitorData {

    @TableId(type = IdType.AUTO)
    private Long id;                 // 主键ID

    private Long pointId;            // 所属监测点ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp; // 数据采集时间

    private Double waterLevel;       // 水位值
    private Double rainfall;         // 降雨量
    private Double flow;             // 流量数据（可选）

    private String otherParams;      // 其他扩展参数（JSON字符串）
}
