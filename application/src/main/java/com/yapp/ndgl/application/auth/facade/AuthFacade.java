package com.yapp.ndgl.application.auth.facade;

import com.yapp.ndgl.application.auth.controller.dto.UserCreateRequest;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateResponse;
import com.yapp.ndgl.application.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;

    public UserCreateResponse createUser(final UserCreateRequest request) {
        return authService.createUser(request);
    }
}
