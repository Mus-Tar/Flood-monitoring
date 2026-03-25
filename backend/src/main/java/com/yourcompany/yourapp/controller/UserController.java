package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.annotation.RequireRole;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.service.UserService;
import com.yourcompany.yourapp.util.R;
import com.yourcompany.yourapp.vo.UserResp;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequireRole("ADMIN")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public R list() {
        List<User> users = userService.list(new QueryWrapper<User>().orderByDesc("id"));
        return R.ok(users.stream().map(UserResp::from).collect(Collectors.toList()));
    }

    @PostMapping
    public R add(@RequestBody User user) {
        String username = user.getUsername() == null ? "" : user.getUsername().trim();
        String password = user.getPassword() == null ? "" : user.getPassword().trim();
        if (username.length() < 3) {
            return R.error("用户名至少需要 3 个字符");
        }
        if (password.length() < 6) {
            return R.error("密码至少需要 6 个字符");
        }
        if (userService.getOne(new QueryWrapper<User>().eq("username", username), false) != null) {
            return R.error("用户名已存在");
        }

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(normalizeRole(user.getRole()));
        userService.save(user);
        return R.ok(UserResp.from(user));
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody User user) {
        User existing = userService.getById(id);
        if (existing == null) {
            return R.error("用户不存在");
        }

        existing.setRole(normalizeRole(user.getRole()));
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            if (user.getPassword().trim().length() < 6) {
                return R.error("密码至少需要 6 个字符");
            }
            existing.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        }

        userService.updateById(existing);
        return R.ok(UserResp.from(existing));
    }

    @DeleteMapping("/{id}")
    public R del(@PathVariable Long id) {
        User existing = userService.getById(id);
        if (existing == null) {
            return R.error("用户不存在");
        }

        if ("ADMIN".equals(existing.getRole())) {
            long adminCount = userService.count(new QueryWrapper<User>().eq("role", "ADMIN"));
            if (adminCount <= 1) {
                return R.error("不能删除最后一个管理员账号");
            }
        }

        userService.removeById(id);
        return R.ok();
    }

    private String normalizeRole(String role) {
        return "ADMIN".equalsIgnoreCase(role) ? "ADMIN" : "USER";
    }
}
