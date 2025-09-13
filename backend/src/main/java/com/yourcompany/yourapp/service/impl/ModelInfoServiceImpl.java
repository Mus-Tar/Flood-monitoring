package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.entity.ModelInfo;
import com.yourcompany.yourapp.mapper.ModelInfoMapper;
import com.yourcompany.yourapp.service.ModelInfoService;
import org.springframework.stereotype.Service;

@Service
public class ModelInfoServiceImpl extends ServiceImpl<ModelInfoMapper, ModelInfo> implements ModelInfoService {
}
