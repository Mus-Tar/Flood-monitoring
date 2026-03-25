package com.yourcompany.yourapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yourcompany.yourapp.entity.RefreshToken;
import com.yourcompany.yourapp.entity.User;

public interface RefreshTokenService extends IService<RefreshToken> {
    RefreshToken createToken(User user);

    RefreshToken getValidToken(String token);

    RefreshToken rotateToken(User user, String token);

    void revokeToken(String token);
}
