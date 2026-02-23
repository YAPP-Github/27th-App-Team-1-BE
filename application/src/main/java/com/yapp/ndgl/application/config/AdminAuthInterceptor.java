package com.yapp.ndgl.application.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Value("${admin.access-token}")
    private String accessToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return true;
        }
        response.sendRedirect("/admin/login");
        return false;
    }

    public boolean validateToken(String token) {
        if (token == null) return false;
        byte[] a = this.accessToken.getBytes(StandardCharsets.UTF_8);
        byte[] b = token.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(a, b);
    }
}
