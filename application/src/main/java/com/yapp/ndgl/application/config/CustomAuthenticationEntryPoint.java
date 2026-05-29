package com.yapp.ndgl.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) throws IOException {

        CommonErrorCode errorCode = CommonErrorCode.UNAUTHORIZED;
        response.setStatus(errorCode.getStatusCode().getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), ErrorResponse.error(errorCode));
    }
}
