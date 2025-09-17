package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.dto.WarningEventVO;
import com.yourcompany.yourapp.entity.MonitoringPoint;
import com.yourcompany.yourapp.entity.WarningEvent;
import com.yourcompany.yourapp.service.MonitoringPointService;
import com.yourcompany.yourapp.service.WarningEventService;
import com.yourcompany.yourapp.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warnings")
public class WarningEventController {

    @Resource
    private WarningEventService service;
    @Resource
    private MonitoringPointService pointService;

    @GetMapping
    public R list(@RequestParam(required = false) Long pointId,
                  @RequestParam(required = false) String status) {

        QueryWrapper<WarningEvent> qw = new QueryWrapper<>();
        if (pointId != null) qw.eq("point_id", pointId);
        if (status != null && !status.isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            qw.eq("status", status);
        }
        qw.orderByDesc("trigger_time");
        List<WarningEvent> list = service.list(qw);

        List<WarningEventVO> vos = list.stream().map(e -> {
            WarningEventVO vo = new WarningEventVO();
            vo.setId(e.getId());
            vo.setPointId(e.getPointId());
            MonitoringPoint p = pointService.getById(e.getPointId());
            vo.setPointName(p == null ? ("#" + e.getPointId()) : p.getName());
            vo.setLevel(e.getLevel());
            vo.setTriggerValue(e.getTriggerValue());
            vo.setTriggerTime(e.getTriggerTime());
            vo.setStatus(e.getStatus());
            vo.setHandledBy(e.getHandledBy());
            return vo;
        }).collect(Collectors.toList());

        return R.ok(vos);
    }

    @PutMapping("/{id}/confirm")
    public R confirm(@PathVariable Long id, @RequestParam(required = false) String user) {
        WarningEvent ev = service.getById(id);
        if (ev == null) return R.error("事件不存在");
        ev.setStatus("已确认");
        if (user != null && !user.isEmpty()) ev.setHandledBy(user);
        service.updateById(ev);
        return R.ok(ev);
    }

    @PutMapping("/{id}/resolve")
    public R resolve(@PathVariable Long id, @RequestParam(required = false) String user) {
        WarningEvent ev = service.getById(id);
        if (ev == null) return R.error("事件不存在");
        ev.setStatus("已解除");
        if (user != null && !user.isEmpty()) ev.setHandledBy(user);
        service.updateById(ev);
        return R.ok(ev);
    }

    /** 新增：删除预警事件 */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        boolean ok = service.removeById(id);
        return ok ? R.ok() : R.error("删除失败或事件不存在");
    }
}
