package com.mall.backend.common.jwt;

import com.mall.backend.common.api.Result;
import com.mall.backend.common.api.ResultCode;
import com.mall.backend.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (isPublicPath(request.getMethod(), uri)) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return unauthorized(response, "请先登录");
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            var claims = JwtUtil.parseToken(token);
            Long userId = Long.valueOf(claims.get("userId").toString());
            String username = claims.get("username").toString();
            Integer role = Integer.valueOf(claims.get("role").toString());
            UserContext.setUserId(userId);
            UserContext.setUsername(username);
            UserContext.setRole(role);
            return true;
        } catch (Exception ex) {
            return unauthorized(response, "登录凭证已失效，请重新登录");
        }
    }

    private boolean isPublicPath(String method, String uri) {
        if (!"GET".equalsIgnoreCase(method)) {
            return false;
        }
        return "/api/product/list".equals(uri)
                || "/api/product/categories".equals(uri)
                || uri.matches("/api/product/\\d+");
    }

    private boolean unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":401,\"message\":\"" + message + "\",\"data\":null}");
        writer.flush();
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
