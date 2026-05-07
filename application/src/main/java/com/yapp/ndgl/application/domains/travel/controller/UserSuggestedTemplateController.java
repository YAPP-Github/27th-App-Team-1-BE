package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.facade.UserSuggestedTemplateFacade;
import com.yapp.ndgl.common.response.SuccessResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/suggested-templates")
@RestController
public class UserSuggestedTemplateController implements UserSuggestedTemplateApi {

    private final UserSuggestedTemplateFacade userSuggestedTemplateFacade;

    @Override
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createUserSuggestedTemplate(
        @CurrentUuid String uuid,
        @Valid @RequestBody final CreateUserSuggestedTemplateRequest request
    ) {
        userSuggestedTemplateFacade.createUserSuggestedTemplate(uuid, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
