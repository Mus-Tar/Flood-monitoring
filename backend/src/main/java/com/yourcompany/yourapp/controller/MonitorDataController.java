package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.MonitorData;
import com.yourcompany.yourapp.service.MonitorDataService;
import com.yourcompany.yourapp.util.R;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
}
