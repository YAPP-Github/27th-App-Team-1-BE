package com.yapp.ndgl.application.domains.travel.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.application.domains.travel.controller.dto.AdminUserSuggestedTemplateResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.common.response.PageResponse;
import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.domain.travel.UserSuggestedTemplate;
import com.yapp.ndgl.application.common.annotation.DistributedLock;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.service.UserSuggestedTemplateDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSuggestedTemplateService {

    private final UserSuggestedTemplateDomainService userSuggestedTemplateDomainService;
    private final TravelTemplateDomainService travelTemplateDomainService;

    @DistributedLock(key = "'suggested_template:' + #videoId")
    @Transactional
    public Long createUserSuggestedTemplate(
        final String uuid,
        final String videoId,
        final CreateUserSuggestedTemplateRequest request
    ) {
        if (travelTemplateDomainService.existsByVideoId(videoId)) {
            throw new GlobalException(TravelErrorCode.ALREADY_REGISTERED_TRAVEL_TEMPLATE);
        }

        userSuggestedTemplateDomainService.findByVideoIdAndStatus(videoId, SuggestionStatus.ACCEPTED)
            .ifPresent(t -> { throw new GlobalException(TravelErrorCode.ALREADY_EXISTS_TRAVEL_TEMPLATE); });

        userSuggestedTemplateDomainService.findByVideoIdAndStatus(videoId, SuggestionStatus.PENDING)
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
                request.category()
            )
        );

        log.info("새로운 여행 영상을 요청하였습니다. userId={}, templateId={}, videoId={}", uuid, template.getId(), videoId);
        return template.getId();
    }

    public void subscribe(final Long templateId, final String uuid) {
        log.info("사용자 제안 여행 템플릿 구독을 신청합니다. templateId={}, subscriberUuid={}", templateId, uuid);
        userSuggestedTemplateDomainService.subscribe(templateId, uuid);
    }

    public PageResponse<AdminUserSuggestedTemplateResponse> readUserSuggestedTemplatesForAdmin(
        final SuggestionStatus status, final int page, final int size
    ) {
        Page<AdminUserSuggestedTemplateResponse> result = userSuggestedTemplateDomainService
            .findUserSuggestedTemplates(status, page, size)
            .map(AdminUserSuggestedTemplateResponse::toResponse);

        return PageResponse.of(
            result.getContent(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements()
        );
    }
}
