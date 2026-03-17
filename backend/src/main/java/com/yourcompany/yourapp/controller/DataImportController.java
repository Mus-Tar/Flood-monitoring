package com.yourcompany.yourapp.controller;

import com.yourcompany.yourapp.entity.MonitorData;
import com.yourcompany.yourapp.entity.MonitoringPoint;
import com.yourcompany.yourapp.service.MonitorDataService;
import com.yourcompany.yourapp.service.MonitoringPointService;
import com.yourcompany.yourapp.service.ThresholdEvaluatorService;
import com.yourcompany.yourapp.util.R;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/monitor-data/import") // 监测数据导入接口
public class DataImportController {

    @Resource
    private MonitorDataService dataService; // 监测数据服务
    @Resource
    private MonitoringPointService pointService; // 监测点服务
    @Resource
    private ThresholdEvaluatorService evaluator; // 阈值评估服务

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping
    public R importForOnePoint(@RequestParam Long pointId,
                               @RequestParam MultipartFile file) throws IOException {
        // 向指定监测点导入历史监测数据
        MonitoringPoint p = pointService.getById(pointId);
        if (p == null) return R.error("监测点不存在");

        String name = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase();
        int total = 0, ok = 0, fail = 0;
        List<String> errors = new ArrayList<>();

        if (name.endsWith(".csv")) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                br.readLine(); // 跳过表头
                String line; int lineNo = 1;
                while ((line = br.readLine()) != null) {
                    lineNo++; total++;
                    String[] arr = line.split(",");
                    if (arr.length < 3) {
                        fail++; errors.add("第" + lineNo + "行列数不足");
                        continue;
                    }
                    try {
                        LocalDateTime ts = LocalDateTime.parse(arr[0].trim(), DF);
                        Double wl = arr[1].trim().isEmpty() ? null : Double.parseDouble(arr[1].trim());
                        Double rf = arr[2].trim().isEmpty() ? null : Double.parseDouble(arr[2].trim());
                        Double fl = (arr.length >= 4 && !arr[3].trim().isEmpty())
                                ? Double.parseDouble(arr[3].trim()) : null;

                        MonitorData md = new MonitorData();
                        md.setPointId(pointId);
                        md.setTimestamp(ts);
                        md.setWaterLevel(wl);
                        md.setRainfall(rf);
                        md.setFlow(fl);
                        dataService.save(md);

                        // 数据入库后立即进行阈值判断并触发预警
                        evaluator.evaluateForData(md);

                        ok++;
                    } catch (Exception ex) {
                        fail++; errors.add("第" + lineNo + "行解析失败");
                    }
                }
            }
        } else if (name.endsWith(".xlsx")) {
            ok += parseXlsxSinglePoint(pointId, file.getInputStream(), errors);
            total = ok + errors.size();
            fail = errors.size();
        } else {
            return R.error("仅支持 .csv 或 .xlsx");
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("total", total);
        resp.put("success", ok);
        resp.put("failed", fail);
        resp.put("errors", errors.size() > 10 ? errors.subList(0, 10) : errors);
        return R.ok(resp);
    }

    private int parseXlsxSinglePoint(Long pointId,
                                     InputStream in,
                                     List<String> errors) throws IOException {
        int ok = 0;
        try (Workbook wb = new XSSFWorkbook(in)) {
            Sheet s = wb.getSheetAt(0);
            for (int i = 1; i <= s.getLastRowNum(); i++) {
                Row r = s.getRow(i);
                if (r == null) continue;
                try {
                    LocalDateTime ts = LocalDateTime.parse(getCellString(r.getCell(0)).trim(), DF);
                    Double wl = getCellString(r.getCell(1)).trim().isEmpty() ? null :
                            Double.valueOf(getCellString(r.getCell(1)));
                    Double rf = getCellString(r.getCell(2)).trim().isEmpty() ? null :
                            Double.valueOf(getCellString(r.getCell(2)));
                    Double fl = getCellString(r.getCell(3)).trim().isEmpty() ? null :
                            Double.valueOf(getCellString(r.getCell(3)));

                    MonitorData md = new MonitorData();
                    md.setPointId(pointId);
                    md.setTimestamp(ts);
                    md.setWaterLevel(wl);
                    md.setRainfall(rf);
                    md.setFlow(fl);
                    dataService.save(md);

                    // 导入数据后执行预警阈值评估
                    evaluator.evaluateForData(md);

                    ok++;
                } catch (Exception ex) {
                    errors.add("第" + (i + 1) + "行解析失败");
                }
            }
        }
        return ok;
    }

    private String getCellString(Cell c) {
        if (c == null) return "";
        if (c.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(c)) {
                return c.getLocalDateTimeCellValue().format(DF);
            }
            return String.valueOf(c.getNumericCellValue());
        }
        return c.toString();
    }
}
