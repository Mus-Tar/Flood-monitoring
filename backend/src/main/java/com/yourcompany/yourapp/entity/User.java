package com.yourcompany.yourapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user") // 对应系统用户表
public class User {

    @TableId(type = IdType.AUTO) // 主键，自增
    private Long id;

    // 登录用户名
    private String username;

    // 登录密码（已加密存储）
    private String password;

    // 用户角色标识（如管理员/普通用户）
    private String role;

    // 创建时间，插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
