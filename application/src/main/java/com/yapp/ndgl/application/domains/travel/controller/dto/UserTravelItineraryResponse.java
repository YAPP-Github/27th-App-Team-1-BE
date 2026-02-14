package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.travel.UserTravelPlace;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserTravelItineraryResponse(
	@Schema(description = "내 여행 여정 목록", requiredMode = Schema.RequiredMode.REQUIRED)
	List<ItineraryPlaceResponse> itineraries
) {
	public static UserTravelItineraryResponse of(
		final List<UserTravelPlace> userTravelPlaces,
		final Map<Long, Place> placeMap,
		final ObjectMapper objectMapper
	) {
		List<ItineraryPlaceResponse> itineraries = userTravelPlaces.stream()
			.map(userTravelPlace -> ItineraryPlaceResponse.of(userTravelPlace, placeMap, objectMapper))
			.toList();
		return new UserTravelItineraryResponse(itineraries);
	}

	public record ItineraryPlaceResponse(
		@Schema(description = "유저 여행 장소 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,
		@Schema(description = "일차", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Integer day,
		@Schema(description = "순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Integer sequence,
		@Schema(description = "여행자 팁", example = "오전 시간 방문 추천", nullable = true)
		String travelerTip,
		@Schema(description = "해당 일차 시작 시간 (null 인 경우 설정 필요)", example = "09:00:00", nullable = true)
		LocalTime startTime,
		@Schema(description = "예상 소요 시간 (분)", example = "60", nullable = true)
		Integer estimatedDuration,
		@Schema(description = "장소 정보", nullable = true)
		PlaceInfo place
	) {
		public static ItineraryPlaceResponse of(
			final UserTravelPlace userTravelPlace,
			final Map<Long, Place> placeMap,
			final ObjectMapper objectMapper
		) {
			Place place = placeMap.get(userTravelPlace.getPlaceId());

			return new ItineraryPlaceResponse(
				userTravelPlace.getId(),
				userTravelPlace.getDay(),
				userTravelPlace.getSequence(),
				userTravelPlace.getTravelerTip(),
				userTravelPlace.getStartTime(),
				userTravelPlace.getEstimatedDuration(),
				place == null ? null : PlaceInfo.from(place, objectMapper)
			);
		}
	}
}
