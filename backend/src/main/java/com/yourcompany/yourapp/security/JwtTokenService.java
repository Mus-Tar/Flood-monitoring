package com.yourcompany.yourapp.security;

import com.yourcompany.yourapp.config.AuthProperties;
import com.yourcompany.yourapp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtTokenService {

    private final AuthProperties authProperties;
    private Key signingKey;

    public JwtTokenService(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(authProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireAt = now.plusMinutes(authProperties.getAccessTokenExpireMinutes());
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuer(authProperties.getIssuer())
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expireAt))
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public AuthUser parseAccessToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .requireIssuer(authProperties.getIssuer())
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        try {
            return new AuthUser(
                    Long.valueOf(claims.getSubject()),
                    claims.get("username", String.class),
                    claims.get("role", String.class)
            );
        } catch (Exception ex) {
            throw new JwtException("Invalid token payload", ex);
        }
    }

    public long getExpiresInSeconds() {
        return authProperties.getAccessTokenExpireMinutes() * 60;
    }

    private Date toDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }
}
