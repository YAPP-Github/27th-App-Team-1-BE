package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalDateTime;

import com.yapp.ndgl.common.type.DomesticRegion;
import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.common.type.TravelCategory;
import com.yapp.ndgl.domain.travel.UserSuggestedTemplate;

public record AdminUserSuggestedTemplateResponse(
    Long id,
    String videoId,
    String videoLink,
    String recommendReason,
    String suggesterUuid,
    TravelCategory category,
    DomesticRegion region,
    SuggestionStatus status,
    LocalDateTime createdAt
) {

    public static AdminUserSuggestedTemplateResponse toResponse(final UserSuggestedTemplate domain) {
        return new AdminUserSuggestedTemplateResponse(
            domain.getId(),
            domain.getVideoId(),
            domain.getVideoLink(),
            domain.getRecommendReason(),
            domain.getSuggesterUuid(),
            domain.getCategory(),
            domain.getRegion(),
            domain.getStatus(),
            domain.getCreatedAt()
        );
    }
}
