package com.yapp.ndgl.application.domains.travel.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.service.UserSuggestedTemplateService;
import com.yapp.ndgl.application.utils.YoutubeUrlParser;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class UserSuggestedTemplateFacade {

    private final UserSuggestedTemplateService userSuggestedTemplateService;

    public void createUserSuggestedTemplate(
        final String uuid,
        final CreateUserSuggestedTemplateRequest request
    ) {
        String videoId = YoutubeUrlParser.extractVideoId(request.videoLink());
        userSuggestedTemplateService.createUserSuggestedTemplate(uuid, videoId, request);
    }

    public void subscribe(final Long templateId, final String uuid) {
        userSuggestedTemplateService.subscribe(templateId, uuid);
    }
}
