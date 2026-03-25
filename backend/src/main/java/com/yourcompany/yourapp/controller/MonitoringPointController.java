package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.annotation.RequireRole;
import com.yourcompany.yourapp.entity.MonitorData;
import com.yourcompany.yourapp.entity.MonitoringPoint;
import com.yourcompany.yourapp.service.MonitorDataService;
import com.yourcompany.yourapp.service.MonitoringPointService;
import com.yourcompany.yourapp.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/points") // 监测点管理接口
public class MonitoringPointController {

    @Resource
    private MonitoringPointService service; // 监测点业务服务

    @Resource
    private MonitorDataService monitorDataService; // 监测数据服务

    @GetMapping
    public R list(@RequestParam(required = false) String keyword) {
        // 查询监测点列表，支持关键字模糊搜索
        QueryWrapper<MonitoringPoint> qw = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            // 按监测点名称、类型、位置、流域、传感器型号匹配
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
    @RequireRole("ADMIN")
    public R add(@RequestBody MonitoringPoint p) {
        // 新增监测点
        service.save(p);
        return R.ok(p);
    }

    @PutMapping("/{id}")
    @RequireRole("ADMIN")
    public R update(@PathVariable Long id, @RequestBody MonitoringPoint p) {
        // 更新监测点基础信息
        p.setId(id);
        service.updateById(p);
        return R.ok(p);
    }

    @DeleteMapping("/{id}")
    @RequireRole("ADMIN")
    public R del(@PathVariable Long id) {
        // 删除监测点前先清除其历史监测数据
        monitorDataService.remove(new QueryWrapper<MonitorData>().eq("point_id", id));
        service.removeById(id);
        return R.ok();
    }
}
