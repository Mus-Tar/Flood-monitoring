package com.yourcompany.yourapp.service;

import com.yourcompany.yourapp.entity.MonitorData;

// 阈值评估服务接口，用于判断监测数据是否触发预警
public interface ThresholdEvaluatorService {

    // 对单条监测数据进行阈值判断处理
    void evaluateForData(MonitorData md);
}
