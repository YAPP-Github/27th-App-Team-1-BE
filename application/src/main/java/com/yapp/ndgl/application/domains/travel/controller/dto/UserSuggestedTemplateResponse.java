package com.yapp.ndgl.application.domains.travel.controller.dto;

import com.yapp.ndgl.common.type.DomesticRegion;
import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.common.type.TravelCategory;
import com.yapp.ndgl.domain.travel.UserSuggestedTemplate;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserSuggestedTemplateResponse(
    @Schema(description = "제안 템플릿 ID", example = "1")
    Long id,

    @Schema(description = "영상 링크")
    String videoLink,

    @Schema(description = "추천 이유")
    String recommendReason,

    @Schema(description = "제안자 UUID")
    String suggesterUuid,

    @Schema(description = "여행 카테고리")
    TravelCategory category,

    @Schema(description = "국내 지역")
    DomesticRegion region,

    @Schema(description = "검토 상태")
    SuggestionStatus status
) {

    public static UserSuggestedTemplateResponse from(final UserSuggestedTemplate domain) {
        return new UserSuggestedTemplateResponse(
            domain.getId(),
            domain.getVideoLink(),
            domain.getRecommendReason(),
            domain.getSuggesterUuid(),
            domain.getCategory(),
            domain.getRegion(),
            domain.getStatus()
        );
    }
}
