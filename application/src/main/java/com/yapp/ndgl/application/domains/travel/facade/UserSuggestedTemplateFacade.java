package com.yapp.ndgl.application.domains.travel.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.AdminUserSuggestedTemplateResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.SubscribeUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.event.publisher.UserSuggestedTemplateEventPublisher;
import com.yapp.ndgl.application.domains.travel.service.UserSuggestedTemplateService;
import com.yapp.ndgl.application.utils.YoutubeUrlParser;
import com.yapp.ndgl.common.response.PageResponse;
import com.yapp.ndgl.common.type.SuggestionStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Facade
@RequiredArgsConstructor
public class UserSuggestedTemplateFacade {

    private final UserSuggestedTemplateService userSuggestedTemplateService;
    private final UserSuggestedTemplateEventPublisher userSuggestedTemplateEventPublisher;

    public void createUserSuggestedTemplate(
        final String uuid,
        final CreateUserSuggestedTemplateRequest request
    ) {
        String videoId = YoutubeUrlParser.extractVideoId(request.videoLink());
        Long templateId = userSuggestedTemplateService.createUserSuggestedTemplate(uuid, videoId, request);

        userSuggestedTemplateEventPublisher.publish(templateId, videoId, uuid, request);
    }

    public void subscribe(final String uuid, final SubscribeUserSuggestedTemplateRequest request) {
        String videoId = YoutubeUrlParser.extractVideoId(request.videoLink());
        userSuggestedTemplateService.subscribe(videoId, uuid);
    }

    public PageResponse<AdminUserSuggestedTemplateResponse> readUserSuggestedTemplatesForAdmin(
        final SuggestionStatus status, final int page, final int size
    ) {
        log.info("어드민 사용자 제안 템플릿 목록을 조회합니다. status = {}, page = {}, size = {}", status, page, size);
        return userSuggestedTemplateService.readUserSuggestedTemplatesForAdmin(status, page, size);
    }
}
