package com.example.bookmanager.interceptor;

import com.example.bookmanager.common.Result;
import com.example.bookmanager.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 放行OPTIONS请求（CORS预检）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        log.info("==================== JWT拦截器 ====================");
        log.info("请求路径: {}", request.getRequestURI());
        log.info("收到的Authorization头: {}", token);

        if (token == null || token.isEmpty()) {
            log.warn("未携带token");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(
                    Result.error(401, "未登录，请先登录")));
            return false;
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
            log.info("去掉Bearer后的token前20位: {}...", token.substring(0, Math.min(20, token.length())));
        } else {
            log.warn("token格式不正确，没有Bearer前缀");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(
                    Result.error(401, "token格式不正确")));
            return false;
        }

        boolean isValid = jwtUtils.validateToken(token);
        log.info("token验证结果: {}", isValid);

        if (!isValid) {
            log.warn("token验证失败，可能已过期或签名不正确");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(
                    Result.error(401, "登录已过期，请重新登录")));
            return false;
        }

        // 将用户信息存入request，方便后续使用
        Claims claims = jwtUtils.parseToken(token);
        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        String role = claims.get("role", String.class);

        log.info("解析token成功 -> userId: {}, username: {}, role: {}", userId, username, role);
        log.info("====================================================");

        request.setAttribute("userId", userId);
        request.setAttribute("username", username);
        request.setAttribute("role", role);

        return true;
    }
}