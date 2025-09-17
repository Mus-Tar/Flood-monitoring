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
        // 添加详细日志
        System.out.println("=== 注册请求开始 ===");
        System.out.println("收到注册请求参数: " + req);
        
        try {
            // 验证输入参数
            if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
                System.out.println("注册失败: 用户名为空");
                return R.error("用户名不能为空");
            }
            if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
                System.out.println("注册失败: 密码为空");
                return R.error("密码不能为空");
            }
            if (req.getUsername().trim().length() < 3) {
                System.out.println("注册失败: 用户名长度不足 - " + req.getUsername().trim().length());
                return R.error("用户名至少需要3个字符");
            }
            if (req.getPassword().trim().length() < 6) {
                System.out.println("注册失败: 密码长度不足 - " + req.getPassword().trim().length());
                return R.error("密码至少需要6个字符");
            }
            
            // 角色验证和规范化
            String role = req.getRole();
            if (role != null) {
                role = role.trim().toUpperCase();
                if (!"USER".equals(role) && !"ADMIN".equals(role)) {
                    System.out.println("注册失败: 无效角色 - " + req.getRole());
                    return R.error("无效的角色类型，只允许USER或ADMIN");
                }
            } else {
                role = "USER"; // 默认角色
            }
            
            // 检查用户名是否已存在
            System.out.println("检查用户名是否存在: " + req.getUsername().trim());
            User existingUser = userService.getOne(new QueryWrapper<User>()
                    .eq("username", req.getUsername().trim()), false);
            if (existingUser != null) {
                System.out.println("注册失败: 用户名已存在 - " + req.getUsername().trim());
                return R.error("用户名已存在");
            }
            
            // 创建新用户
            User newUser = new User();
            newUser.setUsername(req.getUsername().trim());
            newUser.setPassword(req.getPassword().trim()); // 注意：生产环境应该加密密码
            newUser.setRole(role);
            newUser.setCreatedAt(java.time.LocalDateTime.now()); // 手动设置创建时间
            
            System.out.println("准备保存用户到数据库: " + newUser);
            boolean saved = userService.save(newUser);
            System.out.println("数据库保存结果: " + saved);
            
            if (saved) {
                System.out.println("用户注册成功! 用户ID: " + newUser.getId());
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
                System.err.println("用户保存失败: 数据库save()返回false，但无异常抛出");
                return R.error("数据保存失败，请稍后重试");
            }
        } catch (Exception e) {
            System.err.println("注册过程发生异常: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return R.error("系统异常: " + e.getMessage());
        } finally {
            System.out.println("=== 注册请求结束 ===");
        }
    }

    @GetMapping("/test-db")
    public R testDatabase() {
        try {
            System.out.println("测试数据库连接...");
            
            // 测试数据库连接和用户查询
            long userCount = userService.count();
            List<User> recentUsers = userService.list(new QueryWrapper<User>().orderByDesc("id").last("LIMIT 3"));
            
            System.out.println("数据库连接正常，用户总数: " + userCount);
            System.out.println("最近的3个用户: " + recentUsers);
            
            return R.ok()
                .msg("数据库连接正常")
                .data(Map.of(
                    "userCount", userCount,
                    "recentUsers", recentUsers,
                    "timestamp", new java.util.Date()
                ));
        } catch (Exception e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();
            return R.error("数据库连接异常: " + e.getMessage());
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
