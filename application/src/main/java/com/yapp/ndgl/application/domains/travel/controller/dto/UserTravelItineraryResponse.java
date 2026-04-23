package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravelPlace;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserTravelItineraryResponse(
	@Schema(description = "내 여행 여정 목록", requiredMode = Schema.RequiredMode.REQUIRED)
	List<ItineraryPlaceResponse> itineraries
) {
	public static UserTravelItineraryResponse of(
		final List<UserTravelPlace> userTravelPlaces,
		final Map<String, TravelTemplatePlace> templatePlaceMap,
		final Map<Long, Place> placeMap,
		final Map<String, Place> nearbyPlaceMap
	) {
		List<ItineraryPlaceResponse> itineraries = userTravelPlaces.stream()
			.map(userTravelPlace -> ItineraryPlaceResponse.of(
				userTravelPlace,
				templatePlaceMap.get(buildTemplatePlaceKey(userTravelPlace.getDay(), userTravelPlace.getSequence())),
				placeMap,
				nearbyPlaceMap
			))
			.toList();
		return new UserTravelItineraryResponse(itineraries);
	}

	private static String buildTemplatePlaceKey(final Integer day, final Integer sequence) {
		return day + ":" + sequence;
	}

	public record ItineraryPlaceResponse(
		@Schema(description = "유저 여행 장소 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,
		@Schema(description = "일차", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Integer day,
		@Schema(description = "순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Integer sequence,
		@Schema(description = "이전 장소로부터의 거리 (km)", example = "2.5", nullable = true)
		Double distanceKm,
		@Schema(description = "교통수단 목록", nullable = true)
		List<ItineraryTransportationInfo> transportation,
		@Schema(description = "메모", example = "오전 시간 방문 추천", nullable = true)
		String memo,
		@Schema(description = "여행자 팁 목록", example = "[\"오전 시간 방문 추천\", \"현지인 맛집\"]", nullable = true)
		List<String> travelerTips,
		@Schema(description = "대체 장소 목록 (Plan B)", nullable = true)
		List<ItineraryPlanBInfo> planB,
		@Schema(description = "해당 일차 시작 시간 (null 인 경우 설정 필요)", example = "09:00:00", nullable = true)
		LocalTime startTime,
		@Schema(description = "예상 소요 시간 (분)", example = "60", nullable = true)
		Integer estimatedDuration,
		@Schema(description = "예산 (원)", example = "50000", nullable = true)
		Integer budget,
		@Schema(description = "장소 정보", nullable = true)
		PlaceInfo place
	) {
		public static ItineraryPlaceResponse of(
			final UserTravelPlace userTravelPlace,
			final TravelTemplatePlace templatePlace,
			final Map<Long, Place> placeMap,
			final Map<String, Place> nearbyPlaceMap
		) {
			Place place = placeMap.get(userTravelPlace.getPlaceId());
			TravelTemplateItineraryResponse.ItineraryPlaceResponse templateItinerary = templatePlace == null
				? null
				: TravelTemplateItineraryResponse.ItineraryPlaceResponse.of(templatePlace, placeMap, Map.of());

			Double distanceKm = userTravelPlace.getDistanceKm();
			List<ItineraryTransportationInfo> transportation = null;
			if (userTravelPlace.getTransportation() != null) {
				transportation = userTravelPlace.getTransportation().stream()
					.map(t -> new ItineraryTransportationInfo(t.mode(), t.timeMin()))
					.toList();
			}
			List<ItineraryPlanBInfo> planB = null;
			String memo = userTravelPlace.getMemo();
			List<String> travelerTips = templateItinerary == null ? List.of() : templateItinerary.travelerTips();

			if (templateItinerary != null) {
				if (distanceKm == null) {
					distanceKm = templateItinerary.distanceKm();
				}
				if (transportation == null) {
					transportation = templateItinerary.transportation();
				}
				planB = templateItinerary.planB();
			}

			return new ItineraryPlaceResponse(
				userTravelPlace.getId(),
				userTravelPlace.getDay(),
				userTravelPlace.getSequence(),
				distanceKm,
				transportation,
				memo,
				travelerTips,
				planB,
				userTravelPlace.getStartTime(),
				userTravelPlace.getEstimatedDuration() != null
					? userTravelPlace.getEstimatedDuration()
					: templateItinerary == null ? null : templateItinerary.estimatedDuration(),
				userTravelPlace.getBudget(),
				place == null ? null : PlaceInfo.from(place, nearbyPlaceMap)
			);
		}
	}
}
