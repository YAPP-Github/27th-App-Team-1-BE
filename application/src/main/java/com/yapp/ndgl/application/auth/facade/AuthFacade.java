package com.yapp.ndgl.application.auth.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.auth.controller.dto.AuthResponse;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateRequest;
import com.yapp.ndgl.application.auth.controller.dto.UserLoginRequest;
import com.yapp.ndgl.application.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;

    public AuthResponse createUser(final UserCreateRequest request) {
        return authService.createUser(request);
    }

    public AuthResponse login(final UserLoginRequest request) {
        return authService.login(request);
    }
}
