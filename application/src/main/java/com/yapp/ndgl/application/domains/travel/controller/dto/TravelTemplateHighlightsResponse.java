package com.yapp.ndgl.application.domains.travel.controller.dto;

import com.yapp.ndgl.domain.travel.TravelTemplate;

import io.swagger.v3.oas.annotations.media.Schema;

public record TravelTemplateHighlightsResponse(
	@Schema(description = "여행 템플릿 ID", example = "TRAVEL_001")
	String travelId,
	@Schema(description = "국가", example = "일본")
	String country,
	@Schema(description = "도시", example = "도쿄")
	String city,
	@Schema(description = "1인 기준 총 예산 (원)", example = "1,200,000")
	Integer budgetPerPerson,
	@Schema(description = "박 수", example = "3")
	Integer nights,
	@Schema(description = "일 수", example = "4")
	Integer days,
	@Schema(description = "유튜브 영상 정보")
	YoutubeInfo youtube
	// @Schema(description = "여행 템플릿에 포함된 장소 간략 정보")
	// List<TravelTemplatePlace> places
) {

	public static TravelTemplateHighlightsResponse toResponse(
		final TravelTemplate travelTemplate
	) {
		YoutubeInfo youtubeInfo = YoutubeInfo.of(
			travelTemplate.getTitle(), travelTemplate.getYoutuber(), travelTemplate.getThumbnail(), travelTemplate.getProfileImage(),
			travelTemplate.getLink(), travelTemplate.getSummary());

		return new TravelTemplateHighlightsResponse(
			travelTemplate.getTravelId(),
			travelTemplate.getCountry(),
			travelTemplate.getCity(),
			travelTemplate.getBudgetPerPerson(),
			travelTemplate.getNights(),
			travelTemplate.getDays(),
			youtubeInfo
		);
	}

	public record YoutubeInfo(
		@Schema(description = "영상 제목", example = "도쿄 3박 4일 완벽 여행 가이드")
		String title,
		@Schema(description = "유튜버 이름", example = "빠니보틀")
		String youtuber,
		@Schema(description = "유튜버 프로필 이미지 URL", example = "https://example.com/thumbnail/panibottle.jpg")
		String profileImage,
		@Schema(description = "영상 썸네일 URL", example = "https://example.com/thumbnail/tokyo.jpg")
		String thumbnail,
		@Schema(description = "유튜브 링크", example = "https://www.youtube.com/watch?v=tokyo-travel")
		String link,
		@Schema(description = "영상 요약", example = "도쿄 3박 4일 여행의 모든 것. 유튜버가 직접 다녀온 코스로 구성된 완벽한 가이드.")
		String summary
	) {

		public static YoutubeInfo of(
			final String title,
			final String youtuber,
			final String profileImage,
			final String thumbnail,
			final String link,
			final String summary
		) {
			return new YoutubeInfo(
				title,
				youtuber,
				profileImage,
				thumbnail,
				link,
				summary
			);
		}
	}

	// public record TravelTemplatePlace(
	// 	@Schema(description = "순서", example = "1")
	// 	Integer sequence,
	// 	@Schema(description = "일차", example = "1")
	// 	Integer day,
	// 	@Schema(description = "예상 소요 시간(분)", example = "60")
	// 	Integer estimatedDurationMinutes,
	// 	@Schema(description = "유튜버 팁", example = "도쿄 타워는 저녁 시간대 방문하는 것이 좋습니다. 야경이 아름답습니다.")
	// 	String youtuberTip,
	// 	@Schema(description = "장소 정보")
	// 	PlaceInfo place
	// ) {
	// }
	//
	// public record PlaceInfo(
	// 	@Schema(description = "장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg")
	// 	String placeId,
	// 	@Schema(description = "장소 이름", example = "이치란라멘")
	// 	String name,
	// 	@Schema(description = "위도", example = "35.6585805")
	// 	Double latitude,
	// 	@Schema(description = "경도", example = "139.7454329")
	// 	Double longitude,
	// 	@Schema(description = "정규 영업 시간")
	// 	List<String> regularOpeningHours
	// ) {
	// }
}
