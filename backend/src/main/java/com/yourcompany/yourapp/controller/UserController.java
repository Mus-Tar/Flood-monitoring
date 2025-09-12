package com.yourcompany.yourapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.service.UserService;
import com.yourcompany.yourapp.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping
    public R list() {
        List<User> list = userService.list(new QueryWrapper<User>().orderByDesc("id"));
        return R.ok(list);
    }

    @PostMapping
    public R add(@RequestBody User u) {
        userService.save(u);
        return R.ok(u);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody User u) {
        u.setId(id);
        userService.updateById(u);
        return R.ok(u);
    }

    @DeleteMapping("/{id}")
    public R del(@PathVariable Long id) {
        userService.removeById(id);
        return R.ok();
    }
}
