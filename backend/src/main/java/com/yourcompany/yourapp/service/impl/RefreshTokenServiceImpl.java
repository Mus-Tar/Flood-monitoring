package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.config.AuthProperties;
import com.yourcompany.yourapp.entity.RefreshToken;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.mapper.RefreshTokenMapper;
import com.yourcompany.yourapp.service.RefreshTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl extends ServiceImpl<RefreshTokenMapper, RefreshToken> implements RefreshTokenService {

    private final AuthProperties authProperties;

    public RefreshTokenServiceImpl(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public RefreshToken createToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setToken(generateToken());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(authProperties.getRefreshTokenExpireDays()));
        refreshToken.setRevoked(0);
        save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken rotateToken(User user, String token) {
        RefreshToken existing = getValidToken(token);
        if (existing == null) {
            return null;
        }
        existing.setRevoked(1);
        updateById(existing);
        return createToken(user);
    }

    @Override
    public void revokeToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return;
        }
        RefreshToken existing = getOne(new QueryWrapper<RefreshToken>().eq("token", token), false);
        if (existing != null && existing.getRevoked() == 0) {
            existing.setRevoked(1);
            updateById(existing);
        }
    }

    @Override
    public RefreshToken getValidToken(String token) {
        RefreshToken existing = getOne(new QueryWrapper<RefreshToken>().eq("token", token), false);
        if (existing == null || existing.getRevoked() == 1 || existing.getExpiresAt().isBefore(LocalDateTime.now())) {
            return null;
        }
        return existing;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
    }
}
