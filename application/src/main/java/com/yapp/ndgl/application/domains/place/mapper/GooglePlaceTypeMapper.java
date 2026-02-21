package com.yapp.ndgl.application.domains.place.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yapp.ndgl.common.type.PlaceCategory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Google Places API의 primaryType을 서비스 카테고리로 매핑하는 유틸리티.
 * <p>
 * primaryType으로 우선 매핑하고, 매핑되지 않으면 types 리스트로 폴백한다.
 * <p>
 * 매핑 기준: <a href="https://developers.google.com/maps/documentation/places/web-service/place-types?hl=ko">Google Places API Table A</a>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GooglePlaceTypeMapper {

	private static final Map<String, PlaceCategory> TYPE_MAP = new HashMap<>();

	static {
		// 카페
		for (String type : List.of(
			"cafe", "bakery", "cat_cafe", "dog_cafe",
			"coffee_shop", "coffee_stand", "coffee_roastery",
			"tea_house"
		)) {
			TYPE_MAP.put(type, PlaceCategory.CAFE);
		}

		// 공항
		for (String type : List.of("airport", "international_airport")) {
			TYPE_MAP.put(type, PlaceCategory.AIRPORT);
		}

		// 숙소
		for (String type : List.of(
			"bed_and_breakfast", "budget_japanese_inn", "campground", "camping_cabin",
			"cottage", "extended_stay_hotel", "farmstay", "guest_house",
			"hostel", "hotel", "inn", "japanese_inn",
			"lodging", "mobile_home_park", "motel", "private_guest_room",
			"resort_hotel", "rv_park"
		)) {
			TYPE_MAP.put(type, PlaceCategory.ACCOMMODATION);
		}

		// 교통수단
		for (String type : List.of(
			"airstrip", "bike_sharing_station", "bus_station", "bus_stop",
			"ferry_service", "ferry_terminal", "heliport",
			"light_rail_station", "park_and_ride", "parking", "parking_garage", "parking_lot",
			"subway_station", "taxi_service", "taxi_stand", "toll_station",
			"train_station", "train_ticket_office", "tram_stop",
			"transit_depot", "transit_station", "transit_stop",
			"transportation_service", "truck_stop"
		)) {
			TYPE_MAP.put(type, PlaceCategory.TRANSPORT);
		}

		// 음식점 (명시적 타입 + "_restaurant" 접미사는 toCategory에서 별도 처리)
		for (String type : List.of(
			"restaurant", "food", "bar", "bar_and_grill", "beer_garden",
			"bistro", "brewery", "brewpub", "cafeteria",
			"cocktail_bar", "confectionery", "deli", "diner",
			"food_court", "gastropub", "hookah_bar", "lounge_bar",
			"meal_delivery", "meal_takeaway", "pub", "snack_bar",
			"sports_bar", "wine_bar", "winery",
			"acai_shop", "bagel_shop", "cake_shop", "candy_store",
			"chocolate_factory", "chocolate_shop", "dessert_shop",
			"donut_shop", "ice_cream_shop", "juice_shop",
			"kebab_shop", "noodle_shop", "pastry_shop", "salad_shop",
			"sandwich_shop", "hot_dog_stand",
			"food_delivery", "pizza_delivery"
		)) {
			TYPE_MAP.put(type, PlaceCategory.RESTAURANT);
		}
	}

	/**
	 * Google Places의 primaryType으로 우선 매핑하고, 매핑되지 않으면 types 리스트로 폴백한다.
	 *
	 * @param primaryType Google Places primaryType (nullable)
	 * @param types       Google Places types[] (nullable)
	 * @return 매핑된 서비스 카테고리 (절대 null 반환 없음)
	 */
	public static PlaceCategory toCategory(final String primaryType, final List<String> types) {
		PlaceCategory category = resolve(primaryType);
		if (category != PlaceCategory.ATTRACTION) {
			return category;
		}

		if (types != null) {
			for (String type : types) {
				category = resolve(type);
				if (category != PlaceCategory.ATTRACTION) {
					return category;
				}
			}
		}

		return PlaceCategory.ATTRACTION;
	}

	private static PlaceCategory resolve(final String type) {
		if (type == null) {
			return PlaceCategory.ATTRACTION;
		}

		PlaceCategory category = TYPE_MAP.get(type);
		if (category != null) {
			return category;
		}

		if (type.endsWith("_restaurant")) {
			return PlaceCategory.RESTAURANT;
		}

		return PlaceCategory.ATTRACTION;
	}
}
