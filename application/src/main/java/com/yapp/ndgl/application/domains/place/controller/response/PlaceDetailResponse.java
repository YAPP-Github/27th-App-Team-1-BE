package com.yapp.ndgl.application.domains.place.controller.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 장소 세부 정보 최종 응답 DTO.
 */
public record PlaceDetailResponse(
	@Schema(description = "장소 ID", example = "ChIJPR5EUkCNGGARyhvN1EVWEc0", requiredMode = Schema.RequiredMode.REQUIRED)
	String id,
	@Schema(description = "장소 이름", example = "규카츠 모토무라 신주쿠 본점", requiredMode = Schema.RequiredMode.REQUIRED)
	String name,
	@Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.jpg", nullable = true)
	String thumbnail,
	@Schema(description = "국내 전화번호", example = "050-1722-2861", nullable = true)
	String nationalPhoneNumber,
	@Schema(description = "국제 전화번호", example = "+81 50-1722-2861", nullable = true)
	String internationalPhoneNumber,
	@Schema(description = "주소", example = "일본 〒160-0021 Tokyo, Shinjuku City, Kabukichō, 1-chōme−２５−３", nullable = true)
	String formattedAddress,
	@Schema(description = "위치 정보", requiredMode = Schema.RequiredMode.REQUIRED)
	Location location,
	@Schema(description = "사용자 평점 수", example = "7306", nullable = true)
	Integer userRatingCount,
	@Schema(description = "평점", example = "4.9", nullable = true)
	Double rating,
	@Schema(description = "영업시간 목록", nullable = true)
	List<String> regularOpeningHours,
	@Schema(description = "Google Maps URI", example = "https://maps.google.com/?cid=14776686710302251978", nullable = true)
	String googleMapsUri,
	@Schema(description = "웹사이트 URI", example = "https://www.gyukatsu-motomura.com/shop/shinjukuhonten", nullable = true)
	String websiteUri
) {

	public record Location(
		@Schema(description = "위도", example = "35.6946268", requiredMode = Schema.RequiredMode.REQUIRED)
		Double latitude,
		@Schema(description = "경도", example = "139.7016497", requiredMode = Schema.RequiredMode.REQUIRED)
		Double longitude
	) {

		public static Location of(final Double latitude, final Double longitude) {
			return new Location(
				latitude,
				longitude
			);
		}
	}

	public record Photo(
		Integer widthPx,
		Integer heightPx,
		String photoUri
	) {

		public static Photo of(
			final Integer widthPx,
			final Integer heightPx,
			final String photoUri
		) {
			return new Photo(
				widthPx,
				heightPx,
				photoUri
			);
		}
	}

	public static PlaceDetailResponse from(
		final PlaceInfoResponse placeInfoResponse
	) {

		Location location = Location.of(placeInfoResponse.location().latitude(),
			placeInfoResponse.location().longitude());

		return new PlaceDetailResponse(
			placeInfoResponse.id(),
			placeInfoResponse.name(),
			placeInfoResponse.thumbnail(),
			placeInfoResponse.nationalPhoneNumber(),
			placeInfoResponse.internationalPhoneNumber(),
			placeInfoResponse.formattedAddress(),
			location,
			placeInfoResponse.userRatingCount(),
			placeInfoResponse.rating(),
			placeInfoResponse.regularOpeningHours(),
			placeInfoResponse.googleMapsUri(),
			placeInfoResponse.websiteUri()
		);
	}
}
