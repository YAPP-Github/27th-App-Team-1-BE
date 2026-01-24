package com.yapp.ndgl.application.common.logging;

import java.io.IOException;
import java.util.UUID;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@Order(20)
public class MdcFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String USER_ID_KEY = "userId";
    private static final String CLIENT_IP_KEY = "clientIp";
    private static final String SESSION_ID_KEY = "sessionId";
    private static final String USER_ID_ATTRIBUTE = "uuid";
    private static final List<String> CLIENT_IP_HEADERS = List.of(
        "X-Forwarded-For",
        "X-Real-IP",
        "CF-Connecting-IP",
        "True-Client-IP",
        "X-Client-IP",
        "X-Forwarded",
        "Forwarded"
    );

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String requestId = resolveRequestId(request);
        String userId = resolveUserId(request);
        String clientIp = resolveClientIp(request);

        MDC.put(REQUEST_ID_KEY, requestId);
        MDC.put(USER_ID_KEY, userId);
        MDC.put(CLIENT_IP_KEY, clientIp);
        String sessionId = resolveSessionId(request);
        if (sessionId != null) {
            MDC.put(SESSION_ID_KEY, sessionId);
        }

        response.setHeader(REQUEST_ID_HEADER, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (StringUtils.hasText(requestId)) {
            return requestId;
        }
        return UUID.randomUUID().toString();
    }

    private String resolveUserId(HttpServletRequest request) {
        Object attribute = request.getAttribute(USER_ID_ATTRIBUTE);
        if (attribute instanceof String userId && StringUtils.hasText(userId)) {
            return userId;
        }
        return "anonymous";
    }

    private String resolveClientIp(HttpServletRequest request) {
        for (String header : CLIENT_IP_HEADERS) {
            String value = request.getHeader(header);
            if (!StringUtils.hasText(value)) {
                continue;
            }
            String extracted = extractClientIp(header, value);
            if (StringUtils.hasText(extracted)) {
                return extracted;
            }
        }
        return request.getRemoteAddr();
    }

    private String extractClientIp(String header, String value) {
        if ("Forwarded".equalsIgnoreCase(header)) {
            String forwardedFor = extractForwardedFor(value);
            if (StringUtils.hasText(forwardedFor)) {
                return normalizeIp(forwardedFor);
            }
            return null;
        }
        String[] parts = value.split(",");
        for (String part : parts) {
            String candidate = normalizeIp(part);
            if (StringUtils.hasText(candidate) && !"unknown".equalsIgnoreCase(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private String extractForwardedFor(String value) {
        String[] parts = value.split(",");
        for (String part : parts) {
            String token = part.trim();
            int forIndex = token.toLowerCase().indexOf("for=");
            if (forIndex < 0) {
                continue;
            }
            String forValue = token.substring(forIndex + 4).trim();
            if (forValue.startsWith("\"") && forValue.endsWith("\"") && forValue.length() > 1) {
                forValue = forValue.substring(1, forValue.length() - 1);
            }
            return forValue;
        }
        return null;
    }

    private String normalizeIp(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String trimmed = value.trim();
        if ("::1".equals(trimmed)) {
            return "127.0.0.1";
        }
        if (trimmed.startsWith("[")) {
            int end = trimmed.indexOf(']');
            if (end > 0) {
                return trimmed.substring(1, end);
            }
        }
        int colonIndex = trimmed.indexOf(':');
        if (colonIndex > -1 && trimmed.indexOf(':', colonIndex + 1) == -1) {
            return trimmed.substring(0, colonIndex);
        }
        return trimmed;
    }

    private String resolveSessionId(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return null;
        }
        return request.getSession(false).getId();
    }
}
