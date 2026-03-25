package com.yourcompany.yourapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "auth.jwt")
public class AuthProperties {
    private String secret;
    private long accessTokenExpireMinutes = 30;
    private long refreshTokenExpireDays = 7;
    private String issuer = "flood-monitoring";
}
