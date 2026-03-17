package com.yourcompany.yourapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourcompany.yourapp.entity.MonitoringPoint;
import org.apache.ibatis.annotations.Mapper;

// 监测点数据持久层接口
@Mapper
public interface MonitoringPointMapper extends BaseMapper<MonitoringPoint> {
    // 继承通用 CRUD 操作
}
