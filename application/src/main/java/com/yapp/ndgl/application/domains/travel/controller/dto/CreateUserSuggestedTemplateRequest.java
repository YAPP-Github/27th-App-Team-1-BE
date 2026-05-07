package com.yapp.ndgl.application.domains.travel.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yapp.ndgl.common.type.DomesticRegion;
import com.yapp.ndgl.common.type.TravelCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserSuggestedTemplateRequest(
    @Schema(description = "YouTube 영상 링크", example = "https://youtu.be/abc12345678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "영상 링크는 필수입니다.")
    @JsonProperty("video_link")
    String videoLink,

    @Schema(description = "추천 이유", example = "산정호수 일출 명소", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "추천 이유는 필수입니다.")
    @Size(max = 1000, message = "추천 이유는 1000자 이하여야 합니다.")
    @JsonProperty("recommend_reason")
    String recommendReason,

    @Schema(description = "여행 카테고리 (선택)", example = "UNCATEGORIZED")
    TravelCategory category,

    @Schema(description = "국내 지역 (선택)", example = "UNDEFINED")
    DomesticRegion region
) {
}
