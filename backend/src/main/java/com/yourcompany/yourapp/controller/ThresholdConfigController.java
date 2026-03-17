package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.ThresholdConfig;
import com.yourcompany.yourapp.service.ThresholdConfigService;
import com.yourcompany.yourapp.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/thresholds") // 预警阈值配置接口
public class ThresholdConfigController {

    @Resource
    private ThresholdConfigService service; // 阈值配置服务

    @GetMapping
    public R list(@RequestParam(required = false) Long pointId,
                  @RequestParam(required = false) String paramType) {
        // 查询监测点对应的阈值配置
        QueryWrapper<ThresholdConfig> qw = new QueryWrapper<>();
        if (pointId != null) qw.eq("point_id", pointId);
        if (paramType != null && !paramType.isEmpty()) qw.eq("param_type", paramType);
        qw.orderByAsc("point_id", "param_type", "level");
        List<ThresholdConfig> list = service.list(qw);
        return R.ok(list);
    }

    @PostMapping
    public R add(@RequestBody ThresholdConfig cfg) {
        // 新增阈值配置
        service.save(cfg);
        return R.ok(cfg);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody ThresholdConfig cfg) {
        // 更新阈值配置
        cfg.setId(id);
        service.updateById(cfg);
        return R.ok(cfg);
    }

    @DeleteMapping("/{id}")
    public R del(@PathVariable Long id) {
        // 删除指定阈值配置
        service.removeById(id);
        return R.ok();
    }
}
