package com.yourcompany.yourapp.predictor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 预测器工作台占位实现：当前用于历史数据回放演示，验证预测流程
public class LstmPredictorStub implements Predictor {

    @Override
    public List<Double> predict(List<Double> history, List<LocalDateTime> historyTimes,
                                LocalDateTime start, int horizonHours, int stepMinutes) {

        // 按时间范围计算需要展示的数据点数量
        int steps = Math.max(1, (horizonHours * 60) / Math.max(1, stepMinutes));
        List<Double> out = new ArrayList<>(steps);

        // 取最近一段历史数据的均值作为展示基准（非真实预测）
        double base = 0.0;
        int cnt = 0;
        for (int i = Math.max(0, history.size() - 12); i < history.size(); i++) {
            Double v = history.get(i);
            if (v != null) {
                base += v;
                cnt++;
            }
        }
        base = cnt > 0 ? base / cnt : 0.0;

        // 构造平滑变化的数据序列，用于前端趋势展示演示
        for (int i = 0; i < steps; i++) {
            out.add(base + Math.sin(i / 6.28) * 0.01);
        }
        return out;
    }
}
