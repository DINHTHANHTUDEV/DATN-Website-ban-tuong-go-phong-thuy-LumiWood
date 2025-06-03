package com.example.Article.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // áp dụng cho tất cả các endpoint /api/*
                        .allowedOrigins("http://localhost:5173") // cho phép truy cập từ frontend
                        .allowedMethods("*") // cho phép mọi phương thức GET, POST, PUT, DELETE,...
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

