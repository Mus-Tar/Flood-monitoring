package com.yourcompany.yourapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourcompany.yourapp.entity.ThresholdConfig;
import org.apache.ibatis.annotations.Mapper;

// 阈值配置数据持久层接口
@Mapper
public interface ThresholdConfigMapper extends BaseMapper<ThresholdConfig> {
    // 提供阈值配置的基础数据库操作
}
