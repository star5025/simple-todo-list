package org.star5025.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.star5025.backend.interceptor.JwtTokenInterceptor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor; // 你的拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/**") // 需要拦截的路径（如所有/api下的接口）
                .excludePathPatterns("/api/user/login", "/api/user/register","/api/hello"); // 排除登录/注册接口
    }

    /**
     * 配置跨域资源共享（CORS）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*", "http://frontend:5173") // 允许本地开发环境和Docker前端
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 允许的请求方法，添加PATCH
                .allowedHeaders("*") // 允许所有请求头，包括token
                .exposedHeaders("*") // 暴露所有响应头
                .allowCredentials(true) // 允许携带凭证
                .maxAge(3600); // 预检请求的有效期（秒）
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket(){

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Simple-todo-list接口文档")
                .version("1.0")
                .description("Simple-todo-list接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.star5025.backend.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
