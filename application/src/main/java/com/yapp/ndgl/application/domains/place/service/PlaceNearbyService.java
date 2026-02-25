package com.yapp.ndgl.application.domains.place.service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.place.mapper.GooglePlaceTypeMapper;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceDetailClient;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceNearbySearchResponse;
import com.yapp.ndgl.common.type.PlaceCategory;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceNearbyService {

	private static final Set<PlaceCategory> NEARBY_EXCLUDED_CATEGORIES =
		EnumSet.of(PlaceCategory.AIRPORT, PlaceCategory.TRANSPORT);

	private final GoogleMapsPlaceDetailClient googleMapsPlaceDetailClient;
	private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;
	private final PlaceDomainService placeDomainService;
	private final ObjectMapper objectMapper;

	/**
	 * 인근 장소를 조회하여 각각 Place 엔티티로 저장하고,
	 * 원래 장소의 nearby_places_json에 googlePlaceId 목록을 저장한다.
	 *
	 * @param googlePlaceId 기준 장소 ID
	 */
	@Async("nearbyAsyncExecutor")
	public void saveNearbyPlacesIfNotExists(final String googlePlaceId) {
		log.info("인근 장소 저장 시작. googlePlaceId={}", googlePlaceId);

		try {
			Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);

			if (NEARBY_EXCLUDED_CATEGORIES.contains(place.getCategory())) {
				log.debug("인근 장소 조회 제외 카테고리입니다. googlePlaceId={}, category={}", googlePlaceId, place.getCategory());
				return;
			}

			if (StringUtils.hasText(place.getNearbyPlacesJson())) {
				log.debug("이미 인근 장소 정보가 존재합니다. googlePlaceId={}", googlePlaceId);
				return;
			}

			GooglePlaceNearbySearchResponse nearbyResponse = googleMapsPlaceDetailClient.searchNearbyPlaces(
				place.getLatitude(), place.getLongitude());

			if (nearbyResponse == null || nearbyResponse.places() == null || nearbyResponse.places().isEmpty()) {
				log.info("인근 장소 조회 결과가 없습니다. googlePlaceId={}", googlePlaceId);
				return;
			}

			List<String> nearbyGooglePlaceIds = new ArrayList<>();

			for (GooglePlaceNearbySearchResponse.PlaceResult result : nearbyResponse.places()) {
				String nearbyId = result.id();
				if (googlePlaceId.equals(nearbyId)) {
					continue;
				}

				try {
					placeDomainService.saveIfNotExists(toPlace(result));
					nearbyGooglePlaceIds.add(nearbyId);
				} catch (Exception e) {
					log.warn("인근 장소 저장 실패. googlePlaceId={}. 원인: {}", nearbyId, e.getMessage());
				}
			}

			if (!nearbyGooglePlaceIds.isEmpty()) {
				String nearbyPlacesJson = objectMapper.writeValueAsString(nearbyGooglePlaceIds);
				placeDomainService.updateNearbyPlaces(googlePlaceId, nearbyPlacesJson);
				log.info("인근 장소 저장 완료. googlePlaceId={}, count={}", googlePlaceId, nearbyGooglePlaceIds.size());
			}

		} catch (Exception e) {
			log.error("인근 장소 비동기 저장 중 오류 발생. googlePlaceId={}. 인근 장소는 나중에 별도 조회 가능.", googlePlaceId, e);
		}
	}

	private Place toPlace(final GooglePlaceNearbySearchResponse.PlaceResult result) {
		try {
			String name = result.displayName() != null ? result.displayName().text() : null;

			String regularOpeningHours = null;
			if (result.regularOpeningHours() != null && result.regularOpeningHours().weekdayDescriptions() != null) {
				regularOpeningHours = objectMapper.writeValueAsString(
					result.regularOpeningHours().weekdayDescriptions());
			}

			String photosJson = null;
			String thumbnail = null;
			if (result.photos() != null && !result.photos().isEmpty()) {
				photosJson = objectMapper.writeValueAsString(result.photos());
				GooglePlaceNearbySearchResponse.PhotoMeta firstPhoto = result.photos().get(0);
				try {
					thumbnail = googleMapsPlacePhotoClient.getPhotoUri(
						PlacePhotoRequest.of(firstPhoto.name(), firstPhoto.heightPx(), firstPhoto.widthPx())
					).uri();
				} catch (Exception e) {
					log.warn("인근 장소 썸네일 조회 실패. googlePlaceId={}", result.id(), e);
				}
			}

			Double latitude = result.location() != null ? result.location().latitude() : null;
			Double longitude = result.location() != null ? result.location().longitude() : null;

			String priceCurrencyCode = null;
			String priceStartUnits = null;
			String priceEndUnits = null;
			if (result.priceRange() != null) {
				if (result.priceRange().startPrice() != null) {
					priceCurrencyCode = result.priceRange().startPrice().currencyCode();
					priceStartUnits = result.priceRange().startPrice().units();
				}
				if (result.priceRange().endPrice() != null) {
					priceEndUnits = result.priceRange().endPrice().units();
				}
			}

			PlaceCategory category = GooglePlaceTypeMapper.toCategory(result.primaryType(), result.types());

			return Place.create(
				result.id(),
				result.formattedAddress(),
				latitude,
				longitude,
				result.rating(),
				result.nationalPhoneNumber(),
				result.internationalPhoneNumber(),
				result.websiteUri(),
				result.googleMapsUri(),
				result.userRatingCount(),
				name,
				thumbnail,
				regularOpeningHours,
				photosJson,
				priceCurrencyCode,
				priceStartUnits,
				priceEndUnits,
				category
			);
		} catch (Exception e) {
			log.error("인근 장소 변환 실패. googlePlaceId={}", result.id(), e);
			throw new RuntimeException(e);
		}
	}
}
