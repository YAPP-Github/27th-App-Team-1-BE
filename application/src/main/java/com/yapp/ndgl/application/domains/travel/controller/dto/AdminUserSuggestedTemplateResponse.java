package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.common.type.TravelCategory;
import com.yapp.ndgl.domain.travel.UserSuggestedTemplate;

public record AdminUserSuggestedTemplateResponse(
    Long id,
    String videoId,
    String videoLink,
    String recommendReason,
    String suggesterUuid,
    List<TravelCategory> category,
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
            domain.getStatus(),
            domain.getCreatedAt()
        );
    }
}
