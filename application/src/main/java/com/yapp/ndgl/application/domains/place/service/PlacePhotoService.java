package com.yapp.ndgl.application.domains.place.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.application.domains.place.controller.response.PlaceInfoResponse;
import com.yapp.ndgl.application.domains.place.controller.response.PlacePhotoResponse;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.domain.place.PlacePhoto;
import com.yapp.ndgl.domain.place.service.PlacePhotoDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlacePhotoService {

	private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;
	private final PlacePhotoDomainService placePhotoDomainService;

	/**
	 * googlePlaceId에 해당하는 photo URI 목록을 조회한다.
	 * DB에 저장된 photo는 DB에서 조회하고, 없는 photo만 Google API를 호출하여 저장 후 반환한다.
	 *
	 * @param googlePlaceId 장소 ID
	 * @param photoMetas PlaceInfoResponse의 Photo meta 목록
	 * @return photo URI가 포함된 응답
	 */
	public void savePhotoUrls(final String googlePlaceId, final List<PlaceInfoResponse.PhotoMeta> photoMetas) {
		if (photoMetas == null || photoMetas.isEmpty()) {
			return;
		}

		// 1. DB에서 기존 photo 조회
		List<PlacePhoto> existingPhotos = placePhotoDomainService.findByGooglePlaceId(googlePlaceId);

		Map<String, PlacePhoto> existingPhotoMap = existingPhotos.stream()
			.collect(Collectors.toMap(PlacePhoto::getPhotoName, p -> p));

		// 2. photoMetas를 순회하면서 없는 것만 API 호출
		List<PlacePhoto> newPhotos = new ArrayList<>();
		for (PlaceInfoResponse.PhotoMeta photoMeta : photoMetas) {
			if (!existingPhotoMap.containsKey(photoMeta.name())) {
				// Google API 호출
				String uri = fetchPhotoUri(photoMeta);
				PlacePhoto placePhoto = PlacePhoto.create(
					googlePlaceId,
					photoMeta.name(),
					uri,
					photoMeta.widthPx(),
					photoMeta.heightPx()
				);
				newPhotos.add(placePhoto);
			}
		}

		// 3. 새로운 photo만 저장
		if (!newPhotos.isEmpty()) {
			placePhotoDomainService.saveAllIfNotExists(newPhotos);
		}
	}

	public PlacePhotoResponse readPlacePhotoUris(final String googlePlaceId) {
		List<PlacePhoto> photos = placePhotoDomainService.findByGooglePlaceId(googlePlaceId);
		if (photos.isEmpty()) {
			return PlacePhotoResponse.empty();
		}

		return PlacePhotoResponse.toResponse(photos);
	}

	private String fetchPhotoUri(final PlaceInfoResponse.PhotoMeta meta) {
		PlacePhotoRequest request = PlacePhotoRequest.of(
			meta.name(),
			meta.heightPx(),
			meta.widthPx()
		);

		return googleMapsPlacePhotoClient.getPhotoUri(request).uri();

	}
}
