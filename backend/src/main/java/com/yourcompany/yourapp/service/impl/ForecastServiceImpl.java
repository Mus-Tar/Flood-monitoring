package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.entity.Forecast;
import com.yourcompany.yourapp.mapper.ForecastMapper;
import com.yourcompany.yourapp.service.ForecastService;
import org.springframework.stereotype.Service;

@Service
public class ForecastServiceImpl extends ServiceImpl<ForecastMapper, Forecast> implements ForecastService {
}
