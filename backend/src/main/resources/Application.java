package com.yourcompany.yourapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 系统主启动类
@SpringBootApplication
public class Application {

    // 程序入口方法
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(Application.class, args);
    }
}
