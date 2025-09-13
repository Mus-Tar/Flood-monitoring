package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.ModelInfo;
import com.yourcompany.yourapp.service.ModelInfoService;
import com.yourcompany.yourapp.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/models")
public class ModelInfoController {

    @Resource
    private ModelInfoService service;

    @GetMapping
    public R list(@RequestParam(required = false) String modelType,
                  @RequestParam(required = false) String target,
                  @RequestParam(required = false) String status) {
        QueryWrapper<ModelInfo> qw = new QueryWrapper<>();
        if (modelType != null && !modelType.isEmpty()) qw.eq("model_type", modelType);
        if (target != null && !target.isEmpty()) qw.eq("target", target);
        if (status != null && !status.isEmpty()) qw.eq("status", status);
        qw.orderByDesc("id");
        List<ModelInfo> list = service.list(qw);
        return R.ok(list);
    }

    @PostMapping
    public R add(@RequestBody ModelInfo m) {
        service.save(m);
        return R.ok(m);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody ModelInfo m) {
        m.setId(id);
        service.updateById(m);
        return R.ok(m);
    }

    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        service.removeById(id);
        return R.ok();
    }
}
