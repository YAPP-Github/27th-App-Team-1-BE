package com.yapp.ndgl.application.domains.travel.controller.dto;

import com.yapp.ndgl.domain.travel.TravelTemplate;

import io.swagger.v3.oas.annotations.media.Schema;

public record TravelTemplateHighlightsResponse(
	@Schema(description = "여행 템플릿 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long travelId,
	@Schema(description = "국가", example = "JP", requiredMode = Schema.RequiredMode.REQUIRED)
	String country,
	@Schema(description = "도시", example = "도쿄", requiredMode = Schema.RequiredMode.REQUIRED)
	String city,
	@Schema(description = "1인 기준 총 예산 (원)", example = "1200000", nullable = true)
	Integer budgetPerPerson,
	@Schema(description = "박 수", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
	Integer nights,
	@Schema(description = "일 수", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
	Integer days,
	@Schema(description = "유튜브 영상 정보", requiredMode = Schema.RequiredMode.REQUIRED)
	YoutubeInfo youtube,
	@Schema(description = "영상 정보", requiredMode = Schema.RequiredMode.REQUIRED)
	ProgramInfo program

	// @Schema(description = "여행 템플릿에 포함된 장소 간략 정보")
	// List<TravelTemplatePlace> places
) {

	public static TravelTemplateHighlightsResponse toResponse(
		final TravelTemplate travelTemplate
	) {
		String title = travelTemplate.getTitle() == null ? "" : travelTemplate.getTitle();
		String programName = travelTemplate.getTravelProgramName() == null ? "" : travelTemplate.getTravelProgramName();

		YoutubeInfo youtubeInfo = YoutubeInfo.of(
			title,
			programName, // TODO 필드명 변경?
			travelTemplate.getTravelProgramProfileImage(),
			travelTemplate.getThumbnail(),
			travelTemplate.getLink(),
			travelTemplate.getSummary());

		ProgramInfo programInfo = ProgramInfo.of(
			title,
			programName,
			travelTemplate.getTravelProgramProfileImage(),
			travelTemplate.getThumbnail(),
			travelTemplate.getLink(),
			travelTemplate.getSummary());

		return new TravelTemplateHighlightsResponse(
			travelTemplate.getId(),
			travelTemplate.getCountry(),
			travelTemplate.getCity(),
			travelTemplate.getBudgetPerPerson(),
			travelTemplate.getNights(),
			travelTemplate.getDays(),
			youtubeInfo,
			programInfo
		);
	}

	public record YoutubeInfo(
		@Schema(description = "영상 제목", example = "도쿄 3박 4일 완벽 여행 가이드", requiredMode = Schema.RequiredMode.REQUIRED)
		String title,
		@Schema(description = "프로그램명 (유튜버명)", example = "빠니보틀", requiredMode = Schema.RequiredMode.REQUIRED)
		String name,
		@Schema(description = "유튜버 프로필 이미지 URL", example = "https://example.com/thumbnail/panibottle.jpg", nullable = true)
		String profileImage,
		@Schema(description = "영상 썸네일 URL", example = "https://example.com/thumbnail/tokyo.jpg", nullable = true)
		String thumbnail,
		@Schema(description = "영상 링크", example = "https://www.youtube.com/watch?v=tokyo-travel", nullable = true)
		String link,
		@Schema(description = "영상 요약", example = "도쿄 3박 4일 여행의 모든 것. 유튜버가 직접 다녀온 코스로 구성된 완벽한 가이드.", requiredMode = Schema.RequiredMode.REQUIRED)
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

	public record ProgramInfo(
		@Schema(description = "영상 제목", example = "도쿄 3박 4일 완벽 여행 가이드", requiredMode = Schema.RequiredMode.REQUIRED)
		String title,
		@Schema(description = "프로그램명 (유튜버명)", example = "빠니보틀", requiredMode = Schema.RequiredMode.REQUIRED)
		String name,
		@Schema(description = "프로그램 프로필 이미지 URL", example = "https://example.com/thumbnail/panibottle.jpg", nullable = true)
		String profileImage,
		@Schema(description = "영상 썸네일 URL", example = "https://example.com/thumbnail/tokyo.jpg", nullable = true)
		String thumbnail,
		@Schema(description = "영상 링크", example = "https://www.youtube.com/watch?v=tokyo-travel", nullable = true)
		String link,
		@Schema(description = "영상 요약", example = "도쿄 3박 4일 여행의 모든 것. 유튜버가 직접 다녀온 코스로 구성된 완벽한 가이드.", requiredMode = Schema.RequiredMode.REQUIRED)
		String summary
	) {
		public static ProgramInfo of(
			final String title,
			final String name,
			final String profileImage,
			final String thumbnail,
			final String link,
			final String summary
		) {
			return new ProgramInfo(
				title,
				name,
				profileImage,
				thumbnail,
				link,
				summary
			);
		}
	}
}
