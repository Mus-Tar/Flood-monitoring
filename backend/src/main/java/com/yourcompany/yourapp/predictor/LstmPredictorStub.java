package com.yourcompany.yourapp.predictor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 占位：后续你可以改成调用 Python 服务或加载本地模型 */
public class LstmPredictorStub implements Predictor {
    @Override
    public List<Double> predict(List<Double> history, List<LocalDateTime> historyTimes,
                                LocalDateTime start, int horizonHours, int stepMinutes) {
        int steps = Math.max(1, (horizonHours * 60) / Math.max(1, stepMinutes));
        List<Double> out = new ArrayList<>(steps);
        // 这里先用“基线 + 微小随机扰动”的方式占位
        double base = 0.0; int cnt = 0;
        for (int i = Math.max(0, history.size()-12); i < history.size(); i++) {
            Double v = history.get(i);
            if (v != null) { base += v; cnt++; }
        }
        base = cnt>0 ? base/cnt : 0.0;
        for (int i=0;i<steps;i++) out.add(base + Math.sin(i/6.28)*0.01); // 轻微波动
        return out;
    }
}
