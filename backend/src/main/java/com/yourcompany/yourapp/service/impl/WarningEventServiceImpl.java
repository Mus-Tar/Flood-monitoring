package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.entity.WarningEvent;
import com.yourcompany.yourapp.mapper.WarningEventMapper;
import com.yourcompany.yourapp.service.WarningEventService;
import org.springframework.stereotype.Service;

// 预警事件业务实现类
@Service
public class WarningEventServiceImpl
        extends ServiceImpl<WarningEventMapper, WarningEvent>
        implements WarningEventService {
    // 处理预警事件相关业务
}
