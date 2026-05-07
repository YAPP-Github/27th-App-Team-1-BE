package com.yapp.ndgl.application.domains.travel.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserSuggestedTemplateResponse;
import com.yapp.ndgl.application.domains.travel.service.UserSuggestedTemplateService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class UserSuggestedTemplateFacade {

    private final UserSuggestedTemplateService userSuggestedTemplateService;

    public UserSuggestedTemplateResponse createUserSuggestedTemplate(
        final String uuid,
        final CreateUserSuggestedTemplateRequest request
    ) {
        return userSuggestedTemplateService.createUserSuggestedTemplate(uuid, request);
    }
}
