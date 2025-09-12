package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.service.UserService;
import com.yourcompany.yourapp.util.R;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @Data
    static class LoginReq { private String username; private String password; }
    @Data
    static class TokenResp {
        private String token; private String username; private String role;
        TokenResp(String t, String u, String r){ this.token=t; this.username=u; this.role=r; }
    }
}
