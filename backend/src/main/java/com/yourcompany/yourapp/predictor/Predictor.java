package com.yourcompany.yourapp.predictor;

import java.time.LocalDateTime;
import java.util.List;

// 预测工作台统一接口：当前用于数据展示与流程演示，支持后续算法扩展
public interface Predictor {

    // 基于历史时间序列生成趋势数据（当前实现不生成真实未来值）
    List<Double> predict(List<Double> history, List<LocalDateTime> historyTimes,
                         LocalDateTime start, int horizonHours, int stepMinutes);
}
