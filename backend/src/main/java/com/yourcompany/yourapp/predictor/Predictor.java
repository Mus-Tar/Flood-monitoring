package com.yourcompany.yourapp.predictor;

import java.time.LocalDateTime;
import java.util.List;

/** 统一预测器接口；后续把 LSTM/RF 真正接入到实现类即可 */
public interface Predictor {
    /**
     * 基于历史序列做预测
     * @param history 按时间升序的历史值（可能存在 null）
     * @param historyTimes 与 history 对齐的时间戳（升序）
     * @param start 预测起点（含）
     * @param horizonHours 预测时长（小时）
     * @param stepMinutes 预测步长（分钟）
     * @return 预测值（与 times 对齐）
     */
    List<Double> predict(List<Double> history, List<LocalDateTime> historyTimes,
                         LocalDateTime start, int horizonHours, int stepMinutes);
}
