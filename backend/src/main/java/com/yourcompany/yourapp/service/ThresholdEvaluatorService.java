package com.yourcompany.yourapp.service;

import com.yourcompany.yourapp.entity.MonitorData;

public interface ThresholdEvaluatorService {
    void evaluateForData(MonitorData md);
}
