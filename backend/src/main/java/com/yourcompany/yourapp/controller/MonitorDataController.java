package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.annotation.RequireRole;
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
@RequestMapping("/api/monitor-data") // 监测数据管理接口
public class MonitorDataController {

    @Resource
    private MonitorDataService service; // 监测数据服务

    @GetMapping
    public R list(@RequestParam Long pointId,
                  @RequestParam(required = false)
                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                  @RequestParam(required = false)
                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {

        // 查询指定监测点的历史数据
        QueryWrapper<MonitorData> qw = new QueryWrapper<>();
        qw.eq("point_id", pointId);
        if (start != null) qw.ge("timestamp", start);
        if (end != null) qw.le("timestamp", end);
        qw.orderByAsc("timestamp");

        List<MonitorData> list = service.list(qw);
        return R.ok(list);
    }

    @DeleteMapping("/batch")
    @RequireRole("ADMIN")
    public R batchDelete(@RequestBody List<Long> pointIds) {
        // 批量删除指定监测点的历史数据
        if (pointIds == null || pointIds.isEmpty()) {
            return R.error("请选择要删除数据的监测点");
        }

        int totalDeleted = 0;
        Map<Long, Integer> deletedCounts = new HashMap<>();

        for (Long pointId : pointIds) {
            QueryWrapper<MonitorData> qw = new QueryWrapper<>();
            qw.eq("point_id", pointId);
            long count = service.count(qw);
            deletedCounts.put(pointId, (int) count);
            totalDeleted += count;
            service.remove(qw);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalDeleted", totalDeleted);
        result.put("pointCounts", deletedCounts);
        result.put("affectedPoints", pointIds.size());

        return R.ok(result);
    }

    @GetMapping("/count-by-points")
    public R getDataCountByPoints(@RequestParam List<Long> pointIds) {
        // 统计各监测点数据量
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
