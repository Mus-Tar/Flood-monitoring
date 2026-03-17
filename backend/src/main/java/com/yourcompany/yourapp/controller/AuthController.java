package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.service.UserService;
import com.yourcompany.yourapp.util.R;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // 认证相关接口，对应登录与注册功能
public class AuthController {

    @Resource
    private UserService userService; // 用户业务服务

    @PostMapping("/login")
    public R login(@RequestBody LoginReq req) {
        // 根据用户名和密码校验用户身份
        User u = userService.getOne(new QueryWrapper<User>()
                .eq("username", req.getUsername())
                .eq("password", req.getPassword()), false);
        if (u == null) return R.error("用户名或密码错误");
        // 登录成功后返回用户身份信息与令牌
        return R.ok().data(new TokenResp("demo-token", u.getUsername(), u.getRole()));
    }

    // 创建账号，注册
    @PostMapping("/register")
    public R register(@RequestBody RegisterReq req) {
        try {
            // 基础参数合法性校验
            if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
                return R.error("用户名不能为空");
            }
            if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
                return R.error("密码不能为空");
            }
            if (req.getUsername().trim().length() < 3) {
                return R.error("用户名至少需要3个字符");
            }
            if (req.getPassword().trim().length() < 6) {
                return R.error("密码至少需要6个字符");
            }

            // 角色校验与默认角色设置
            String role = req.getRole();
            if (role != null) {
                role = role.trim().toUpperCase();
                if (!"USER".equals(role) && !"ADMIN".equals(role)) {
                    return R.error("无效的角色类型，只允许USER或ADMIN");
                }
            } else {
                role = "USER"; // 默认普通用户角色
            }

            // 检查用户名唯一性
            User existingUser = userService.getOne(new QueryWrapper<User>()
                    .eq("username", req.getUsername().trim()), false);
            if (existingUser != null) {
                return R.error("用户名已存在");
            }

            // 创建并保存新用户
            User newUser = new User();
            newUser.setUsername(req.getUsername().trim());
            newUser.setPassword(req.getPassword().trim());
            newUser.setRole(role);
            newUser.setCreatedAt(java.time.LocalDateTime.now());

            boolean saved = userService.save(newUser);

            if (saved) {
                // 注册成功后返回用户基本信息
                return R.ok()
                        .msg("注册成功")
                        .data(Map.of(
                                "id", newUser.getId(),
                                "username", newUser.getUsername(),
                                "role", newUser.getRole(),
                                "createdAt", newUser.getCreatedAt().toString()
                        ));
            } else {
                return R.error("数据保存失败，请稍后重试");
            }
        } catch (Exception e) {
            return R.error("系统异常");
        }
    }

    // 登录请求参数封装
    @Data
    static class LoginReq {
        private String username;
        private String password;
    }

    // 注册请求参数封装
    @Data
    static class RegisterReq {
        private String username;
        private String password;
        private String role;
    }

    // 登录成功返回数据封装
    @Data
    static class TokenResp {
        private String token;
        private String username;
        private String role;

        TokenResp(String t, String u, String r) {
            this.token = t;
            this.username = u;
            this.role = r;
        }
    }
}
