package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.MonitorData;
import com.yourcompany.yourapp.entity.MonitoringPoint;
import com.yourcompany.yourapp.service.MonitorDataService;
import com.yourcompany.yourapp.service.MonitoringPointService;
import com.yourcompany.yourapp.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class MonitoringPointController {

    @Resource
    private MonitoringPointService service;

    @Resource
    private MonitorDataService monitorDataService;

    @GetMapping
    public R list(@RequestParam(required = false) String keyword) {
        QueryWrapper<MonitoringPoint> qw = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            // 依据你的字段：name / type / location / river_basin / sensor_model
            qw.like("name", keyword)
                    .or().like("type", keyword)
                    .or().like("location", keyword)
                    .or().like("river_basin", keyword)
                    .or().like("sensor_model", keyword);
        }
        qw.orderByDesc("id");
        List<MonitoringPoint> list = service.list(qw);
        return R.ok(list);
    }

    @PostMapping
    public R add(@RequestBody MonitoringPoint p) {
        service.save(p);
        return R.ok(p);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody MonitoringPoint p) {
        p.setId(id);
        service.updateById(p);
        return R.ok(p);
    }

    @DeleteMapping("/{id}")
    public R del(@PathVariable Long id) {
        // 你的外键没有 ON DELETE CASCADE，需先删子表数据
        monitorDataService.remove(new QueryWrapper<MonitorData>().eq("point_id", id));
        service.removeById(id);
        return R.ok();
    }
}
