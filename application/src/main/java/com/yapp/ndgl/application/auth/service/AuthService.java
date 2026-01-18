package com.yapp.ndgl.application.auth.service;

import com.yapp.ndgl.domain.user.User;
import org.springframework.stereotype.Service;
import com.yapp.ndgl.application.auth.controller.dto.AuthResponse;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateRequest;
import com.yapp.ndgl.application.auth.controller.dto.UserLoginRequest;
import com.yapp.ndgl.application.auth.component.JwtTokenProvider;
import com.yapp.ndgl.domain.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDomainService userDomainService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse createUser(final UserCreateRequest request) {
        User user = userDomainService.createUser(
            request.fcmToken(),
            request.deviceModel(),
            request.deviceOs(),
            request.deviceOsVersion(),
            request.appVersion()
        );

        String accessToken = jwtTokenProvider.generateToken(user.getUuid());

        return new AuthResponse(user.getUuid(), accessToken, user.getNickname());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(final UserLoginRequest request) {
        User user = userDomainService.findByUuid(request.uuid());

        String accessToken = jwtTokenProvider.generateToken(request.uuid());

        return new AuthResponse(request.uuid(), accessToken, user.getNickname());
    }
}

