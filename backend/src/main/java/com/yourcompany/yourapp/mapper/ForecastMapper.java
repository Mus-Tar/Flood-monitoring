package com.yourcompany.yourapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourcompany.yourapp.entity.Forecast;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForecastMapper extends BaseMapper<Forecast> {
}
