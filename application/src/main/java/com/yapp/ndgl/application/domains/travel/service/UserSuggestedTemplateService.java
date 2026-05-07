package com.yapp.ndgl.application.domains.travel.service;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserSuggestedTemplateResponse;
import com.yapp.ndgl.clients.google.youtube.YouTubeDataClient;
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

    public UserSuggestedTemplateResponse createUserSuggestedTemplate(
        final String uuid,
        final CreateUserSuggestedTemplateRequest request
    ) {
        log.info("사용자 제안 여행 템플릿을 등록합니다. suggesterUuid = {}", uuid);

        // 영상 URL 형식 검증 + videoId 추출 (Gemini 분석은 별도 스코프)
        String videoId = youTubeDataClient.extractVideoId(request.videoLink());

        UserSuggestedTemplate userSuggestedTemplate = userSuggestedTemplateDomainService.create(
            UserSuggestedTemplate.of(
                videoId,
                request.videoLink(),
                request.recommendReason(),
                uuid,
                request.category(),
                request.region()
            )
        );

        return UserSuggestedTemplateResponse.from(userSuggestedTemplate);
    }
}
