package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.MonitorData;
import com.yourcompany.yourapp.entity.ThresholdConfig;
import com.yourcompany.yourapp.entity.WarningEvent;
import com.yourcompany.yourapp.mapper.MonitorDataMapper;
import com.yourcompany.yourapp.service.ThresholdConfigService;
import com.yourcompany.yourapp.service.ThresholdEvaluatorService;
import com.yourcompany.yourapp.service.WarningEventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThresholdEvaluatorServiceImpl implements ThresholdEvaluatorService {

    @Resource
    private ThresholdConfigService thresholdConfigService;
    @Resource
    private WarningEventService warningEventService;
    @Resource
    private MonitorDataMapper monitorDataMapper;

    @Override
    public void evaluateForData(MonitorData md) {
        if (md == null || md.getPointId() == null || md.getTimestamp() == null) return;

        List<ThresholdConfig> configs = thresholdConfigService.list(
                new QueryWrapper<ThresholdConfig>().eq("point_id", md.getPointId())
        );
        if (configs.isEmpty()) return;

        // 分别对两类参数评估，只触发各自的“最高级别”
        evaluateForParam(md, configs, "WATER_LEVEL", md.getWaterLevel());
        evaluateForParam(md, configs, "RAINFALL", md.getRainfall());
    }

    /** 只触发该参数类型中“已超阈”的最高 level */
    private void evaluateForParam(MonitorData md, List<ThresholdConfig> configs, String paramType, Double value) {
        if (value == null) return;

        ThresholdConfig best = null; // 最高级别的配置
        for (ThresholdConfig cfg : configs) {
            if (!paramType.equalsIgnoreCase(cfg.getParamType())) continue;
            if (cfg.getThresholdValue() == null) continue;
            if (value <= cfg.getThresholdValue()) continue;

            // 持续时间判断（duration<=0 表示单点触发）
            if (!passesDuration(md, cfg, paramType)) continue;

            if (best == null || (cfg.getLevel() != null && cfg.getLevel() > best.getLevel())) {
                best = cfg;
            }
        }

        if (best != null) {
            triggerIfNotDuplicate(md, best, value);
        }
    }

    /** 持续时间窗口内，所有样本都要超阈（和你之前的规则保持一致） */
    private boolean passesDuration(MonitorData md, ThresholdConfig cfg, String paramType) {
        int dur = cfg.getDuration() == null ? 0 : cfg.getDuration();
        if (dur <= 0) return true;

        LocalDateTime start = md.getTimestamp().minusMinutes(dur);
        QueryWrapper<MonitorData> qw = new QueryWrapper<MonitorData>()
                .eq("point_id", md.getPointId())
                .ge("timestamp", start)
                .le("timestamp", md.getTimestamp())
                .orderByAsc("timestamp");
        List<MonitorData> win = monitorDataMapper.selectList(qw);
        if (win.isEmpty()) return false;

        for (MonitorData d : win) {
            Double v = "WATER_LEVEL".equalsIgnoreCase(paramType) ? d.getWaterLevel() : d.getRainfall();
            if (v == null || v <= cfg.getThresholdValue()) return false;
        }
        return true;
    }

    /** 近10分钟内、同点+同级别且未解除存在则不重复触发 */
    private void triggerIfNotDuplicate(MonitorData md, ThresholdConfig cfg, Double triggerValue) {
        LocalDateTime tenMinAgo = md.getTimestamp().minusMinutes(10);
        WarningEvent last = warningEventService.getOne(
                new QueryWrapper<WarningEvent>()
                        .eq("point_id", md.getPointId())
                        .eq("level", cfg.getLevel())
                        .ne("status", "已解除")
                        .ge("trigger_time", tenMinAgo)
                        .orderByDesc("trigger_time")
                        .last("limit 1"),
                false
        );
        if (last != null) return;

        WarningEvent ev = new WarningEvent();
        ev.setPointId(md.getPointId());
        ev.setLevel(cfg.getLevel());
        ev.setTriggerValue(triggerValue);
        ev.setTriggerTime(md.getTimestamp());
        ev.setStatus("未处理");
        warningEventService.save(ev);
    }
}
