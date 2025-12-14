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
public class JwtTokenInterceptor implements HandlerInterceptor {

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

        // 对于OPTIONS预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());

        // 2、校验令牌
        try {
            // 检查token是否存在
            if (token == null || token.isEmpty()) {
                log.warn("请求缺少token: {}", request.getRequestURI());
                response.setStatus(401);
                return false;
            }
            
            log.info("jwt校验:{}", token);
            // 解析Token（使用你的密钥）
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);

            // 3、从claims中提取用户ID
            Long userId = Long.valueOf(claims.get("userId").toString());
            log.info("当前用户id：{}", userId);

            // 4、将用户ID存入上下文
            BaseContext.setCurrentId(userId);

            // 校验通过，放行
            return true;
        } catch (Exception ex) {
            // 校验失败，响应401
            log.error("Token校验失败: ", ex);
            response.setStatus(401);
            return false;
        }
    }
}