package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.entity.MonitoringPoint;
import com.yourcompany.yourapp.mapper.MonitoringPointMapper;
import com.yourcompany.yourapp.service.MonitoringPointService;
import org.springframework.stereotype.Service;

@Service
public class MonitoringPointServiceImpl
        extends ServiceImpl<MonitoringPointMapper, MonitoringPoint>
        implements MonitoringPointService {
}
