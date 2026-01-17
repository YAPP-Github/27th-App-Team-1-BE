package com.yapp.ndgl.application.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateRequest;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateResponse;
import com.yapp.ndgl.application.auth.facade.AuthFacade;
import com.yapp.ndgl.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth", description = "인증 관련 API")
@RequiredArgsConstructor
@RestController
public class AuthController implements AuthApi {

    private final AuthFacade authFacade;

    @Override
    public ResponseEntity<SuccessResponse<UserCreateResponse>> createUser(
        final @Valid @RequestBody UserCreateRequest request) {
        UserCreateResponse response = authFacade.createUser(request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}

