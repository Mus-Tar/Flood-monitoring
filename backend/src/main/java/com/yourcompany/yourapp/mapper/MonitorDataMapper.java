package com.yourcompany.yourapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourcompany.yourapp.entity.MonitorData;
import org.apache.ibatis.annotations.Mapper;

// 监测数据数据访问接口
@Mapper
public interface MonitorDataMapper extends BaseMapper<MonitorData> {
    // 提供监测数据的基础 CRUD 操作
}
