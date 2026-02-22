package com.yapp.ndgl.application.domains.travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItineraryPlanBInfo(
	@Schema(description = "Google Place ID", example = "ChIJN1t_tDeuEmsRUsoyG83frY4", requiredMode = Schema.RequiredMode.REQUIRED)
	String googlePlaceId,
	@Schema(description = "장소명", example = "콜로세움 (Colosseo)", requiredMode = Schema.RequiredMode.REQUIRED)
	String name,
	@Schema(description = "장소 썸네일 이미지 URL", example = "https://places.googleapis.com/v1/places/ChIJN1t_tDeuEmsRUsoyG83frY4/photos/...", nullable = true)
	String thumbnail,
	@Schema(description = "장소 카테고리", example = "TOURIST_ATTRACTION", nullable = true)
	String category
) {
}
