package org.star5025.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.star5025.backend.interceptor.JwtTokenInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor; // 你的拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/**") // 需要拦截的路径（如所有/api下的接口）
                .excludePathPatterns("/api/user/login", "/api/user/register"); // 排除登录/注册接口
    }
}