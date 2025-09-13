package com.yourcompany.yourapp.predictor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 占位：后续可改成基于树模型的回归推理 */
public class RfPredictorStub implements Predictor {
    @Override
    public List<Double> predict(List<Double> history, List<LocalDateTime> historyTimes,
                                LocalDateTime start, int horizonHours, int stepMinutes) {
        int steps = Math.max(1, (horizonHours * 60) / Math.max(1, stepMinutes));
        List<Double> out = new ArrayList<>(steps);
        double last = 0.0; boolean ok = false;
        for (int i = history.size()-1; i>=0; i--) {
            Double v = history.get(i);
            if (v != null) { last = v; ok = true; break; }
        }
        double base = ok ? last : 0.0;
        for (int i=0;i<steps;i++) out.add(base);
        return out;
    }
}
