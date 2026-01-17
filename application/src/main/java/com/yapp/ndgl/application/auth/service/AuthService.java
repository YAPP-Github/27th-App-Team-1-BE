package com.yapp.ndgl.application.auth.service;

import org.springframework.stereotype.Service;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateRequest;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateResponse;
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
    public UserCreateResponse createUser(final UserCreateRequest request) {
        String uuid = userDomainService.createUser(
            request.fcmToken(),
            request.deviceModel(),
            request.deviceOs(),
            request.deviceOsVersion(),
            request.appVersion()
        );

        String accessToken = jwtTokenProvider.generateToken(uuid);

        return new UserCreateResponse(uuid, accessToken);
    }
}

