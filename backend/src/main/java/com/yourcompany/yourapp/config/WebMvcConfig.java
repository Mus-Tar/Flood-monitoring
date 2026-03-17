package com.yourcompany.yourapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Web MVC 相关配置
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 文件上传目录（支持配置文件覆盖）
    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    // 配置静态资源映射规则
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射上传文件访问路径
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(
                        // 本地文件系统路径
                        "file:" + uploadDir + "/",
                        // 类路径静态资源目录
                        "classpath:/static.upload/"
                );
        // 前端页面由静态资源机制统一处理
    }
}
