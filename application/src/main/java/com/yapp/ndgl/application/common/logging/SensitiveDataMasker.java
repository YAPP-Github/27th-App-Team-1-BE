package com.yapp.ndgl.application.common.logging;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

final class SensitiveDataMasker {

    private static final String FULL_MASK = "***";

    private static final Set<String> SENSITIVE_HEADER_NAMES = Set.of(
        "authorization",
        "x-api-key",
        "api-key",
        "token",
        "access-token",
        "refresh-token"
    );

    private static final Pattern SENSITIVE_JSON_FIELD =
        Pattern.compile("(?i)\"(password|passwd|pwd|secret|token|key|auth|authorization)\"\\s*:\\s*\"(.*?)\"");

    private SensitiveDataMasker() {
    }

    static Map<String, String> maskHeaders(Map<String, String> headers) {
        Map<String, String> masked = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (isSensitiveHeader(name)) {
                masked.put(name, FULL_MASK);
            } else {
                masked.put(name, value);
            }
        }
        return masked;
    }

    static String maskBody(String body) {
        if (body == null || body.isBlank()) {
            return body;
        }
        return SENSITIVE_JSON_FIELD.matcher(body).replaceAll("\"$1\":\"" + FULL_MASK + "\"");
    }

    private static boolean isSensitiveHeader(String headerName) {
        if (headerName == null) {
            return false;
        }
        String normalized = headerName.toLowerCase(Locale.ROOT);
        if (SENSITIVE_HEADER_NAMES.contains(normalized)) {
            return true;
        }
        return normalized.contains("authorization")
            || normalized.contains("token")
            || normalized.contains("secret")
            || normalized.contains("password")
            || normalized.contains("key")
            || normalized.contains("auth");
    }
}
