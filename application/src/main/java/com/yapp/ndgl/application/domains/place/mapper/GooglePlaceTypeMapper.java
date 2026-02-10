package com.yapp.ndgl.application.domains.place.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.yapp.ndgl.domain.place.PlaceCategory;

/**
 * Google Places API의 primaryType/types를 서비스 태그로 매핑하는 유틸리티.
 */
public class GooglePlaceTypeMapper {

	private static final Set<String> CAFE_TYPES = Set.of("cafe", "bakery");
	private static final Set<String> AIRPORT_TYPES = Set.of("airport");
	private static final Set<String> ACCOMMODATION_TYPES = Set.of(
		"lodging", "hotel", "motel", "hostel", "resort_hotel", "guest_house"
	);
	private static final Set<String> TRANSPORT_TYPES = Set.of(
		"train_station", "subway_station", "bus_station", "transit_station",
		"light_rail_station", "taxi_stand", "ferry_terminal", "parking"
	);
	private static final Set<String> RESTAURANT_TYPES = Set.of(
		"restaurant", "meal_takeaway", "meal_delivery", "bar",
		"fast_food_restaurant", "food"
	);

	private GooglePlaceTypeMapper() {
	}

	/**
	 * Google Places의 primaryType과 types[]를 서비스 태그로 매핑한다.
	 *
	 * @param primaryType Google Places primaryType (nullable)
	 * @param types       Google Places types[] (nullable)
	 * @return 매핑된 서비스 태그 (절대 null 반환 없음)
	 */
	public static PlaceCategory toCategory(final String primaryType, final List<String> types) {
		List<String> candidates = buildCandidates(primaryType, types);

		// Step 1: 카페 강제 규칙 (cafe/bakery가 어떤 상황에서도 최우선)
		if (candidates.stream().anyMatch(CAFE_TYPES::contains)) {
			return PlaceCategory.CAFE;
		}

		// Step 2: 우선순위 기반 매핑 (공항 → 숙소 → 교통수단 → 음식점)
		if (candidates.stream().anyMatch(AIRPORT_TYPES::contains)) {
			return PlaceCategory.AIRPORT;
		}
		if (candidates.stream().anyMatch(ACCOMMODATION_TYPES::contains)) {
			return PlaceCategory.ACCOMMODATION;
		}
		if (candidates.stream().anyMatch(TRANSPORT_TYPES::contains)) {
			return PlaceCategory.TRANSPORT;
		}
		if (candidates.stream().anyMatch(RESTAURANT_TYPES::contains)) {
			return PlaceCategory.RESTAURANT;
		}

		// Step 3: 기본값 (그 외 모든 타입은 관광명소)
		return PlaceCategory.ATTRACTION;
	}

	private static List<String> buildCandidates(final String primaryType, final List<String> types) {
		List<String> candidates = new ArrayList<>();
		if (primaryType != null) {
			candidates.add(primaryType);
		}
		if (types != null) {
			candidates.addAll(types);
		}
		return candidates;
	}
}
