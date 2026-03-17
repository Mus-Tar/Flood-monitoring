package com.yourcompany.yourapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourcompany.yourapp.entity.WarningEvent;
import org.apache.ibatis.annotations.Mapper;

// 预警事件数据持久层接口
@Mapper
public interface WarningEventMapper extends BaseMapper<WarningEvent> {
    // 用于预警事件的持久化操作
}
