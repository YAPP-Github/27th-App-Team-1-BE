package com.yapp.ndgl.application.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.yapp.ndgl.application.auth.controller.dto.AuthResponse;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateRequest;
import com.yapp.ndgl.application.auth.controller.dto.UserLoginRequest;
import com.yapp.ndgl.application.auth.facade.AuthFacade;
import com.yapp.ndgl.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController implements AuthApi {

    private final AuthFacade authFacade;

    @Override
    public ResponseEntity<SuccessResponse<AuthResponse>> createUser(
        @Valid @RequestBody UserCreateRequest request) {
        AuthResponse response = authFacade.createUser(request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Override
    public ResponseEntity<SuccessResponse<AuthResponse>> login(
        @Valid @RequestBody UserLoginRequest request) {
        AuthResponse response = authFacade.login(request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}

