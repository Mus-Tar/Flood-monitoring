package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("model_info")
public class ModelInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String modelType;     // LSTM / RF / 其他
    private String target;        // WATER_LEVEL / RAINFALL / ...
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trainStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trainEnd;
    /** JSON 文本，后续可换成对象 */
    private String parameters;
    private String status;        // 训练中 / 已完成 / 已发布 / 失败 ...
    private String modelPath;
    /** JSON 文本，评估指标 */
    private String evalMetrics;
}
