package com.yapp.ndgl.application.domains.place.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SearchPlaceRequest(
	@Schema(description = "Google Places 장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "googlePlaceId는 필수입니다")
	String googlePlaceId
) {
}
