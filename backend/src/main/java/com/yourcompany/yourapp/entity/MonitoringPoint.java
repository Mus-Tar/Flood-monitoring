package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("monitoring_point")
public class MonitoringPoint {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;         // 监测点名称
    private String type;         // 类型（雨量站/水位站/等）
    private String location;     // 位置（经纬度或区域）
    private String riverBasin;   // 所属流域/河流（DB列：river_basin）
    private Double installHeight;// 安装高度（米）
    private String sensorModel;  // 传感器型号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // DB列：created_at（默认CURRENT_TIMESTAMP）
}
