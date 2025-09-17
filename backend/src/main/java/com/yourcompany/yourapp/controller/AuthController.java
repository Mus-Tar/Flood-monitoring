package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.service.UserService;
import com.yourcompany.yourapp.util.R;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody LoginReq req) {
        User u = userService.getOne(new QueryWrapper<User>()
                .eq("username", req.getUsername())
                .eq("password", req.getPassword()), false);
        if (u == null) return R.error("用户名或密码错误");
        return R.ok().data(new TokenResp("demo-token", u.getUsername(), u.getRole()));
    }

    @PostMapping("/register")
    public R register(@RequestBody RegisterReq req) {
        try {
            // 验证输入参数
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
            
            // 角色验证和规范化
            String role = req.getRole();
            if (role != null) {
                role = role.trim().toUpperCase();
                if (!"USER".equals(role) && !"ADMIN".equals(role)) {
                    return R.error("无效的角色类型，只允许USER或ADMIN");
                }
            } else {
                role = "USER"; // 默认角色
            }
            
            // 检查用户名是否已存在
            User existingUser = userService.getOne(new QueryWrapper<User>()
                    .eq("username", req.getUsername().trim()), false);
            if (existingUser != null) {
                return R.error("用户名已存在");
            }
            
            // 创建新用户
            User newUser = new User();
            newUser.setUsername(req.getUsername().trim());
            newUser.setPassword(req.getPassword().trim()); // 注意：生产环境应该加密密码
            newUser.setRole(role);
            newUser.setCreatedAt(java.time.LocalDateTime.now());
            
            boolean saved = userService.save(newUser);
            
            if (saved) {
                // 返回成功信息，不包含密码
                return R.ok()
                    .msg("注册成功")
                    .data(Map.of(
                        "id", newUser.getId(),
                        "username", newUser.getUsername(),
                        "role", newUser.getRole(),
                        "createdAt", newUser.getCreatedAt() != null ? newUser.getCreatedAt().toString() : "刚刚"
                    ));
            } else {
                return R.error("数据保存失败，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("系统异常: " + e.getMessage());
        }
    }

    @Data
    static class LoginReq { private String username; private String password; }
    @Data
    static class RegisterReq { private String username; private String password; private String role; }
    @Data
    static class TokenResp {
        private String token; private String username; private String role;
        TokenResp(String t, String u, String r){ this.token=t; this.username=u; this.role=r; }
    }
}
