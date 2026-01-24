package com.yapp.ndgl.application.common.logging;

import static net.logstash.logback.argument.StructuredArguments.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.support.logging.SensitiveDataMasker;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(30)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_SIZE = 5 * 1024;
    private static final Set<String> BODY_METHODS = Set.of("POST", "PUT", "PATCH");
    private static final List<String> EXCLUDE_PATTERNS = List.of(
        "/actuator/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/swagger",
        "/favicon.ico",
        "/static/**",
        "/public/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String pattern : EXCLUDE_PATTERNS) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        boolean debugEnabled = log.isDebugEnabled();

        HttpServletRequest requestToUse = request;
        if (debugEnabled || BODY_METHODS.contains(request.getMethod())) {
            if (!(request instanceof ContentCachingRequestWrapper)) {
                requestToUse = new ContentCachingRequestWrapper(request);
            }
        }

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestToUse, responseWrapper);
        } finally {
            logRequestAndResponse(requestToUse, responseWrapper, startTime, debugEnabled);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequestAndResponse(
        HttpServletRequest request,
        ContentCachingResponseWrapper response,
        long startTime,
        boolean debugEnabled
    ) {
        try {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            String method = request.getMethod();
            String uri = request.getRequestURI();
            Map<String, Object> params = buildParams(request);

            Object requestBody = formatBody(readRequestBody(request), request.getContentType());
            Object responseBody = formatBody(readResponseBody(response), response.getContentType());

            if (debugEnabled) {
                log.debug("API 요청 완료",
                    kv("method", method),
                    kv("uri", uri),
                    kv("status", status),
                    kv("duration", duration),
                    kv("params", params),
                    kv("headers", SensitiveDataMasker.maskHeaders(extractHeaders(request))),
                    kv("requestBody", requestBody),
                    kv("responseBody", responseBody),
                    kv("queryString", request.getQueryString())
                );
                return;
            }

            if (status >= 500) {
                log.error("API 요청 실패",
                    kv("method", method),
                    kv("uri", uri),
                    kv("status", status),
                    kv("duration", duration),
                    kv("params", params),
                    kv("requestBody", requestBody),
                    kv("responseBody", responseBody)
                );
            } else if (status >= 400) {
                log.warn("API 요청 실패",
                    kv("method", method),
                    kv("uri", uri),
                    kv("status", status),
                    kv("duration", duration),
                    kv("params", params),
                    kv("requestBody", requestBody),
                    kv("responseBody", responseBody)
                );
            } else {
                log.info("API 요청 완료",
                    kv("method", method),
                    kv("uri", uri),
                    kv("status", status),
                    kv("duration", duration),
                    kv("params", params)
                );
            }
        } catch (Exception loggingException) {
            log.warn("API 요청 로깅 실패", loggingException);
        }
    }

    private Map<String, Object> buildParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap == null || parameterMap.isEmpty()) {
            return Map.of();
        }
        Map<String, Object> params = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values == null) {
                params.put(key, null);
            } else if (values.length == 1) {
                params.put(key, values[0]);
            } else {
                List<String> list = new ArrayList<>();
                for (String value : values) {
                    list.add(value);
                }
                params.put(key, list);
            }
        }
        return params;
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new LinkedHashMap<>();
        var names = request.getHeaderNames();
        while (names != null && names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers;
    }

    private String readRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] content = wrapper.getContentAsByteArray();
            if (content.length == 0) {
                return null;
            }
            if (isBinaryContent(wrapper.getContentType())) {
                return "[non-text request body omitted]";
            }
            return new String(content, resolveCharset(wrapper.getContentType(), wrapper.getCharacterEncoding()));
        }
        return null;
    }

    private String readResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length == 0) {
            return null;
        }
        if (isBinaryContent(response.getContentType())) {
            return "[non-text response body omitted]";
        }
        return new String(content, resolveCharset(response.getContentType(), response.getCharacterEncoding()));
    }

    private Object formatBody(String body, String contentType) {
        if (!StringUtils.hasText(body)) {
            return body;
        }
        String masked = SensitiveDataMasker.maskBody(body);
        if (masked.length() > MAX_BODY_SIZE) {
            return masked.substring(0, MAX_BODY_SIZE)
                + " (truncated, original size: " + masked.length() + " bytes)";
        }
        if (!isJsonContent(contentType)) {
            return masked;
        }
        try {
            JsonNode node = OBJECT_MAPPER.readTree(masked);
            return node;
        } catch (Exception ignore) {
            return masked;
        }
    }

    private boolean isBinaryContent(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return false;
        }
        String lower = contentType.toLowerCase(Locale.ROOT);
        return !(lower.startsWith("text/")
            || lower.contains("json")
            || lower.contains("xml")
            || lower.contains("javascript")
            || lower.contains("html"));
    }

    private boolean isJsonContent(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return false;
        }
        return contentType.toLowerCase(Locale.ROOT).contains("json");
    }

    private java.nio.charset.Charset resolveCharset(String contentType, String charset) {
        String resolved = charset;
        if (!StringUtils.hasText(resolved) && StringUtils.hasText(contentType)) {
            String lower = contentType.toLowerCase(Locale.ROOT);
            int index = lower.indexOf("charset=");
            if (index > -1) {
                resolved = contentType.substring(index + "charset=".length()).trim();
            }
        }
        if (!StringUtils.hasText(resolved) || "iso-8859-1".equalsIgnoreCase(resolved)) {
            return StandardCharsets.UTF_8;
        }
        return java.nio.charset.Charset.forName(resolved);
    }
}
