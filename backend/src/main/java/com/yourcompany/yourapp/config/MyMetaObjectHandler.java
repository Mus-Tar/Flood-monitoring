package com.yourcompany.yourapp.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// MyBatis-Plus 元数据自动填充处理器
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    // 新增数据时自动填充字段
    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间
        this.strictInsertFill(
                metaObject,
                "createdAt",
                LocalDateTime.class,
                LocalDateTime.now()
        );
    }

    // 更新数据时的自动填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        // 本系统更新操作不处理创建时间
    }
}
