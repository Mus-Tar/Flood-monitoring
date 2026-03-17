package com.yourcompany.yourapp.predictor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 随机森林风格预测器占位实现：用于演示多算法结构与接口设计
public class RfPredictorStub implements Predictor {

    @Override
    public List<Double> predict(List<Double> history, List<LocalDateTime> historyTimes,
                                LocalDateTime start, int horizonHours, int stepMinutes) {

        // 根据展示时间范围计算数据点数量
        int steps = Math.max(1, (horizonHours * 60) / Math.max(1, stepMinutes));
        List<Double> out = new ArrayList<>(steps);

        // 使用最近一次有效监测数据作为展示起点
        double last = 0.0;
        boolean ok = false;
        for (int i = history.size() - 1; i >= 0; i--) {
            Double v = history.get(i);
            if (v != null) {
                last = v;
                ok = true;
                break;
            }
        }
        double base = ok ? last : 0.0;

        // 输出稳定序列，用于预测工作台界面演示
        for (int i = 0; i < steps; i++) {
            out.add(base);
        }
        return out;
    }
}
