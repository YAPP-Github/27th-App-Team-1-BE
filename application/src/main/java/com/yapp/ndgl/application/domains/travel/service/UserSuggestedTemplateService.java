package com.yapp.ndgl.application.domains.travel.service;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.clients.google.youtube.YouTubeDataClient;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.domain.travel.UserSuggestedTemplate;
import com.yapp.ndgl.domain.travel.service.UserSuggestedTemplateDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSuggestedTemplateService {

    private final UserSuggestedTemplateDomainService userSuggestedTemplateDomainService;
    private final YouTubeDataClient youTubeDataClient;

    public void createUserSuggestedTemplate(
        final String uuid,
        final CreateUserSuggestedTemplateRequest request
    ) {
        log.info("사용자 제안 여행 템플릿을 등록합니다. suggesterUuid = {}", uuid);

        String videoId = youTubeDataClient.extractVideoId(request.videoLink());

        userSuggestedTemplateDomainService.findByVideoId(videoId)
            .ifPresent(t -> {
                if (t.getSuggesterUuid().equals(uuid)) {
                    throw new GlobalException(TravelErrorCode.ALREADY_REQUESTED_SUGGESTED_TEMPLATE);
                }
                throw new GlobalException(TravelErrorCode.ALREADY_EXISTS_SUGGESTED_TEMPLATE);
            });

        UserSuggestedTemplate template = userSuggestedTemplateDomainService.create(
            UserSuggestedTemplate.of(
                videoId,
                request.videoLink(),
                request.recommendReason(),
                uuid,
                request.category(),
                request.region()
            )
        );

        log.info("새로운 여행 영상을 요청하였습니다. userId = {}, templateId = {}, video = {}", uuid, template.getId(), videoId);
    }

    public void subscribe(final Long templateId, final String uuid) {
        log.info("사용자 제안 여행 템플릿 구독을 신청합니다. templateId = {}, subscriberUuid = {}", templateId, uuid);
        userSuggestedTemplateDomainService.subscribe(templateId, uuid);
    }
}
