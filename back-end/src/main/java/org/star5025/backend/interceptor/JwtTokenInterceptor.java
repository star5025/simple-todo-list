package org.star5025.backend.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.star5025.backend.context.BaseContext;
import org.star5025.backend.properties.JwtProperties;
import org.star5025.backend.utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor { // 类名可根据你的场景修改（比如去掉Admin）

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是Controller方法还是其他资源（如静态资源）
        if (!(handler instanceof HandlerMethod)) {
            return true; // 非动态方法，直接放行
        }

        // 1、从请求头中获取令牌（不处理Bearer前缀，直接取原始值）
        // 注意：这里的getAdminTokenName()需与你的JwtProperties字段匹配，如果你只有一种Token，可改为getTokenName()
        String token = request.getHeader(jwtProperties.getTokenName());

        // 2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            // 解析Token（使用你的密钥）
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);

            // 3、从claims中提取你的业务键名（使用userId作为键名）

            Long userId = Long.valueOf(claims.get("userId").toString()); // 这里的"userId"是你的实际键名
            log.info("当前用户id：{}", userId);

            // 4、将用户ID存入上下文（BaseContext需适配你的类型，比如如果是String则改类型）
            BaseContext.setCurrentId(userId);

            // 校验通过，放行
            return true;
        } catch (Exception ex) {
            // 校验失败，响应401
            response.setStatus(401);
            return false;
        }
    }
}