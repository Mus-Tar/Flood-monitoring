package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.RefreshToken;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.security.AuthContext;
import com.yourcompany.yourapp.security.AuthUser;
import com.yourcompany.yourapp.security.JwtTokenService;
import com.yourcompany.yourapp.service.RefreshTokenService;
import com.yourcompany.yourapp.service.UserService;
import com.yourcompany.yourapp.util.R;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;
    @Resource
    private RefreshTokenService refreshTokenService;
    @Resource
    private JwtTokenService jwtTokenService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public R login(@RequestBody LoginReq req) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", req.getUsername()), false);
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return R.error("用户名或密码错误");
        }

        RefreshToken refreshToken = refreshTokenService.createToken(user);
        return R.ok(buildTokenPayload(user, refreshToken));
    }

    @PostMapping("/register")
    public R register(@RequestBody RegisterReq req) {
        String username = req.getUsername() == null ? "" : req.getUsername().trim();
        String password = req.getPassword() == null ? "" : req.getPassword().trim();

        if (username.isEmpty()) {
            return R.error("用户名不能为空");
        }
        if (username.length() < 3) {
            return R.error("用户名至少需要 3 个字符");
        }
        if (password.isEmpty()) {
            return R.error("密码不能为空");
        }
        if (password.length() < 6) {
            return R.error("密码至少需要 6 个字符");
        }
        if (userService.getOne(new QueryWrapper<User>().eq("username", username), false) != null) {
            return R.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        userService.save(user);

        return R.ok()
                .msg("注册成功")
                .data(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "role", user.getRole(),
                        "createdAt", String.valueOf(user.getCreatedAt())
                ));
    }

    @PostMapping("/refresh")
    public R refresh(@RequestBody RefreshReq req) {
        String refreshTokenValue = req == null || req.getRefreshToken() == null ? "" : req.getRefreshToken().trim();
        if (refreshTokenValue.isEmpty()) {
            return R.error("refreshToken 不能为空");
        }

        RefreshToken existing = refreshTokenService.getValidToken(refreshTokenValue);
        if (existing == null) {
            return R.error("refreshToken 无效或已过期");
        }

        User user = userService.getById(existing.getUserId());
        if (user == null) {
            return R.error("用户不存在");
        }

        RefreshToken rotated = refreshTokenService.rotateToken(user, refreshTokenValue);
        return R.ok(buildTokenPayload(user, rotated));
    }

    @PostMapping("/logout")
    public R logout(@RequestBody(required = false) RefreshReq req) {
        if (req != null) {
            refreshTokenService.revokeToken(req.getRefreshToken());
        }
        return R.ok().msg("退出成功");
    }

    @GetMapping("/check-username")
    public R checkUsername(@RequestParam String username) {
        String normalized = username == null ? "" : username.trim();
        if (normalized.isEmpty()) {
            return R.error("用户名不能为空");
        }
        boolean available = userService.getOne(new QueryWrapper<User>().eq("username", normalized), false) == null;
        return R.ok(Map.of("available", available));
    }

    @GetMapping("/me")
    public R me() {
        AuthUser authUser = AuthContext.get();
        if (authUser == null) {
            return R.error("未登录");
        }
        User user = userService.getById(authUser.getUserId());
        if (user == null) {
            return R.error("用户不存在");
        }
        return R.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
        ));
    }

    private Map<String, Object> buildTokenPayload(User user, RefreshToken refreshToken) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("role", user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", jwtTokenService.createAccessToken(user));
        result.put("refreshToken", refreshToken.getToken());
        result.put("tokenType", "Bearer");
        result.put("expiresIn", jwtTokenService.getExpiresInSeconds());
        result.put("userInfo", userInfo);
        return result;
    }

    @Data
    static class LoginReq {
        private String username;
        private String password;
    }

    @Data
    static class RegisterReq {
        private String username;
        private String password;
    }

    @Data
    static class RefreshReq {
        private String refreshToken;
    }
}
