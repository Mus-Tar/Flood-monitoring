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
// 阈值评估与预警触发服务实现类
public class ThresholdEvaluatorServiceImpl implements ThresholdEvaluatorService {

    @Resource
    private ThresholdConfigService thresholdConfigService;
    @Resource
    private WarningEventService warningEventService;
    @Resource
    private MonitorDataMapper monitorDataMapper;

    @Override
    // 对单条监测数据进行阈值判断与预警评估
    public void evaluateForData(MonitorData md) {
        // 基础合法性校验
        if (md == null || md.getPointId() == null || md.getTimestamp() == null) return;

        // 查询该监测点对应的全部阈值配置
        List<ThresholdConfig> configs = thresholdConfigService.list(
                new QueryWrapper<ThresholdConfig>().eq("point_id", md.getPointId())
        );
        if (configs.isEmpty()) return;

        // 分别对不同参数类型进行评估（各自仅触发最高等级）
        evaluateForParam(md, configs, "WATER_LEVEL", md.getWaterLevel());
        evaluateForParam(md, configs, "RAINFALL", md.getRainfall());
    }

    // 针对指定参数类型进行阈值判断，仅保留超阈的最高等级
    private void evaluateForParam(MonitorData md, List<ThresholdConfig> configs, String paramType, Double value) {
        if (value == null) return;

        ThresholdConfig best = null; // 当前参数触发的最高等级阈值
        for (ThresholdConfig cfg : configs) {
            // 过滤非当前参数类型的阈值配置
            if (!paramType.equalsIgnoreCase(cfg.getParamType())) continue;
            if (cfg.getThresholdValue() == null) continue;
            // 未超过阈值则不触发
            if (value <= cfg.getThresholdValue()) continue;

            // 判断是否满足持续时间规则
            if (!passesDuration(md, cfg, paramType)) continue;

            // 选择等级最高的阈值配置
            if (best == null || (cfg.getLevel() != null && cfg.getLevel() > best.getLevel())) {
                best = cfg;
            }
        }

        // 触发预警（避免重复）
        if (best != null) {
            triggerIfNotDuplicate(md, best, value);
        }
    }

    // 判断在配置的持续时间窗口内是否持续超阈
    private boolean passesDuration(MonitorData md, ThresholdConfig cfg, String paramType) {
        int dur = cfg.getDuration() == null ? 0 : cfg.getDuration();
        // 持续时间为0表示单点判断
        if (dur <= 0) return true;

        // 计算持续时间窗口起点
        LocalDateTime start = md.getTimestamp().minusMinutes(dur);
        QueryWrapper<MonitorData> qw = new QueryWrapper<MonitorData>()
                .eq("point_id", md.getPointId())
                .ge("timestamp", start)
                .le("timestamp", md.getTimestamp())
                .orderByAsc("timestamp");
        // 查询窗口内的监测数据
        List<MonitorData> win = monitorDataMapper.selectList(qw);
        if (win.isEmpty()) return false;

        // 窗口内所有数据均需超过阈值
        for (MonitorData d : win) {
            Double v = "WATER_LEVEL".equalsIgnoreCase(paramType) ? d.getWaterLevel() : d.getRainfall();
            if (v == null || v <= cfg.getThresholdValue()) return false;
        }
        return true;
    }

    // 在规定时间内避免同一监测点、同等级预警重复触发
    private void triggerIfNotDuplicate(MonitorData md, ThresholdConfig cfg, Double triggerValue) {
        // 定义重复触发判断时间窗口
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
        // 已存在未解除预警则不重复生成
        if (last != null) return;

        // 生成新的预警事件记录
        WarningEvent ev = new WarningEvent();
        ev.setPointId(md.getPointId());
        ev.setLevel(cfg.getLevel());
        ev.setTriggerValue(triggerValue);
        ev.setTriggerTime(md.getTimestamp());
        ev.setStatus("未处理");
        warningEventService.save(ev);
    }
}
