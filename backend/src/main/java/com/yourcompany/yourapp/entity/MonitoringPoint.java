package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("monitoring_point")
// 监测点基础信息实体
public class MonitoringPoint {

    @TableId(type = IdType.AUTO)
    private Long id;                 // 主键ID

    private String name;             // 监测点名称
    private String type;             // 监测点类型（雨量/水位等）
    private String location;         // 监测点位置描述
    private String riverBasin;       // 所属流域或河流
    private Double installHeight;    // 设备安装高度（米）
    private String sensorModel;      // 传感器型号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 创建时间
}
