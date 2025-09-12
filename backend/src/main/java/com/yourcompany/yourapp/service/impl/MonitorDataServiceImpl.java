package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.entity.MonitorData;
import com.yourcompany.yourapp.mapper.MonitorDataMapper;
import com.yourcompany.yourapp.service.MonitorDataService;
import org.springframework.stereotype.Service;

@Service
public class MonitorDataServiceImpl extends ServiceImpl<MonitorDataMapper, MonitorData>
        implements MonitorDataService {
}
