package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.entity.ThresholdConfig;
import com.yourcompany.yourapp.mapper.ThresholdConfigMapper;
import com.yourcompany.yourapp.service.ThresholdConfigService;
import org.springframework.stereotype.Service;

@Service
public class ThresholdConfigServiceImpl extends ServiceImpl<ThresholdConfigMapper, ThresholdConfig>
        implements ThresholdConfigService {
}
