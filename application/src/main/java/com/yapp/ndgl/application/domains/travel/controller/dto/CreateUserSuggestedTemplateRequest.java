package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.util.List;

import com.yapp.ndgl.common.type.TravelCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateUserSuggestedTemplateRequest(
    @Schema(description = "YouTube 영상 링크", example = "https://youtu.be/abc12345678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "영상 링크는 필수입니다.")
    String videoLink,

    @Schema(description = "추천 이유 (선택)", example = "산정호수 일출 명소")
    @Size(max = 1000, message = "추천 이유는 1000자 이하여야 합니다.")
    String recommendReason,

    @Schema(description = "여행 카테고리 (복수 선택)", example = "[\"FOOD\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "카테고리는 최소 한 개 이상 선택해야 합니다.")
    List<TravelCategory> category
) {
}
