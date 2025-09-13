package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.ThresholdConfig;
import com.yourcompany.yourapp.service.ThresholdConfigService;
import com.yourcompany.yourapp.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/thresholds")
public class ThresholdConfigController {

    @Resource
    private ThresholdConfigService service;

    @GetMapping
    public R list(@RequestParam(required = false) Long pointId,
                  @RequestParam(required = false) String paramType) {
        QueryWrapper<ThresholdConfig> qw = new QueryWrapper<>();
        if (pointId != null) qw.eq("point_id", pointId);
        if (paramType != null && !paramType.isEmpty()) qw.eq("param_type", paramType);
        qw.orderByAsc("point_id", "param_type", "level");
        List<ThresholdConfig> list = service.list(qw);
        return R.ok(list);
    }

    @PostMapping
    public R add(@RequestBody ThresholdConfig cfg) {
        service.save(cfg);
        return R.ok(cfg);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody ThresholdConfig cfg) {
        cfg.setId(id);
        service.updateById(cfg);
        return R.ok(cfg);
    }

    @DeleteMapping("/{id}")
    public R del(@PathVariable Long id) {
        service.removeById(id);
        return R.ok();
    }
}
