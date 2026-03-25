package com.yourcompany.yourapp.vo;

import com.yourcompany.yourapp.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResp {
    private Long id;
    private String username;
    private String role;
    private LocalDateTime createdAt;

    public static UserResp from(User user) {
        UserResp resp = new UserResp();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRole(user.getRole());
        resp.setCreatedAt(user.getCreatedAt());
        return resp;
    }
}
