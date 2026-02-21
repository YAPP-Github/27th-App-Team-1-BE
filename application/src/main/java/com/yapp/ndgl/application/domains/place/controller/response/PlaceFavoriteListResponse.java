package com.yapp.ndgl.application.domains.place.controller.response;

import com.yapp.ndgl.common.type.PlaceCategory;
import com.yapp.ndgl.domain.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceFavoriteListResponse(
    @Schema(description = "장소 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "Google Places 장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg", requiredMode = Schema.RequiredMode.REQUIRED)
    String googlePlaceId,
    @Schema(description = "주소", example = "일본 〒160-0021 Tokyo, Shinjuku City, Kabukichō, ...", nullable = true)
    String formattedAddress,
    @Schema(description = "위도", example = "35.6946268", requiredMode = Schema.RequiredMode.REQUIRED)
    Double latitude,
    @Schema(description = "경도", example = "139.7016497", requiredMode = Schema.RequiredMode.REQUIRED)
    Double longitude,
    @Schema(description = "평점", example = "4.9", nullable = true)
    Double rating,
    @Schema(description = "사용자 평점 수", example = "7306", nullable = true)
    Integer userRatingCount,
    @Schema(description = "장소명", example = "규카츠 모토무라 신주쿠 본점", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,
    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.jpg", nullable = true)
    String thumbnail,
    @Schema(description = "장소 카테고리", example = "RESTAURANT", requiredMode = Schema.RequiredMode.REQUIRED)
    PlaceCategory category
) {

    public static PlaceFavoriteListResponse from(final Place place) {
        return new PlaceFavoriteListResponse(
            place.getId(),
            place.getGooglePlaceId(),
            place.getFormattedAddress(),
            place.getLatitude(),
            place.getLongitude(),
            place.getRating(),
            place.getUserRatingCount(),
            place.getName(),
            place.getThumbnail(),
            place.getCategory()
        );
    }
}
