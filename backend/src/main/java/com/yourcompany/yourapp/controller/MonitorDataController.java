package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.MonitorData;
import com.yourcompany.yourapp.service.MonitorDataService;
import com.yourcompany.yourapp.util.R;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor-data")
public class MonitorDataController {

    @Resource
    private MonitorDataService service;

    @GetMapping
    public R list(@RequestParam Long pointId,
                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        QueryWrapper<MonitorData> qw = new QueryWrapper<>();
        qw.eq("point_id", pointId);
        if (start != null) qw.ge("timestamp", start);
        if (end != null) qw.le("timestamp", end);
        qw.orderByAsc("timestamp");
        List<MonitorData> list = service.list(qw);
        return R.ok(list);
    }

    /**
     * 批量删除监测点数据
     */
    @DeleteMapping("/batch")
    public R batchDelete(@RequestBody List<Long> pointIds) {
        if (pointIds == null || pointIds.isEmpty()) {
            return R.error("请选择要删除数据的监测点");
        }

        int totalDeleted = 0;
        Map<Long, Integer> deletedCounts = new HashMap<>();

        for (Long pointId : pointIds) {
            QueryWrapper<MonitorData> qw = new QueryWrapper<>();
            qw.eq("point_id", pointId);
            
            // 先统计要删除的数量
            long count = service.count(qw);
            deletedCounts.put(pointId, (int) count);
            totalDeleted += count;
            
            // 删除数据
            service.remove(qw);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalDeleted", totalDeleted);
        result.put("pointCounts", deletedCounts);
        result.put("affectedPoints", pointIds.size());

        return R.ok(result);
    }

    /**
     * 获取监测点数据统计
     */
    @GetMapping("/count-by-points")
    public R getDataCountByPoints(@RequestParam List<Long> pointIds) {
        Map<Long, Integer> counts = new HashMap<>();
        int total = 0;

        for (Long pointId : pointIds) {
            QueryWrapper<MonitorData> qw = new QueryWrapper<>();
            qw.eq("point_id", pointId);
            long count = service.count(qw);
            counts.put(pointId, (int) count);
            total += count;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("pointCounts", counts);
        result.put("totalCount", total);

        return R.ok(result);
    }
}
