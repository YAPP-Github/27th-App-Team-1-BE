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
	@Deprecated
	@Schema(description = "유튜브 영상 정보(해당 필드는 Deprecated 되었습니다. program 필드를 사용해주셔야 합니다.)", requiredMode = Schema.RequiredMode.REQUIRED, deprecated = true)
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
}
