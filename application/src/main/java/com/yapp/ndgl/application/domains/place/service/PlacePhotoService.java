package com.yapp.ndgl.application.domains.place.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.application.domains.place.dto.PlacePhotoUrisResponse;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;
import com.yapp.ndgl.domain.place.PlacePhoto;
import com.yapp.ndgl.domain.place.PlacePhotoDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlacePhotoService {

	private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;
	private final PlacePhotoDomainService placePhotoDomainService;

	/**
	 * placeId에 해당하는 photo URI 목록을 조회한다.
	 * DB에 저장된 데이터가 있으면 DB에서 조회하고, 없으면 Google API를 호출하여 저장 후 반환한다.
	 *
	 * @param placeId 장소 ID
	 * @param photoMetas GooglePlaceDetailsResponse의 Photo meta 목록
	 * @return photo URI가 포함된 응답
	 */
	public PlacePhotoUrisResponse getPhotoUris(final String placeId, final List<GooglePlaceDetailsResponse.PhotoMeta> photoMetas) {
		// 1. DB에서 조회
		List<PlacePhoto> existingPhotos = placePhotoDomainService.findByPlaceId(placeId);
		if (!existingPhotos.isEmpty()) {
			return toResponse(existingPhotos);
		}

		// 2. DB에 없으면 Google API 호출
		if (photoMetas == null || photoMetas.isEmpty()) {
			return PlacePhotoUrisResponse.empty();
		}

		List<PlacePhotoUrisResponse.PhotoUri> uris = photoMetas.stream()
			.map(photoMeta -> {
				String uri = fetchPhotoUri(photoMeta);
				return PlacePhotoUrisResponse.PhotoUri.of(
					photoMeta.name(),
					photoMeta.widthPx(),
					photoMeta.heightPx(),
					uri
				);
			}).toList();

		// 3. DB에 저장
		List<PlacePhoto> placePhotos = uris.stream()
			.map(uri -> PlacePhoto.create(
				placeId,
				uri.name(),
				uri.photoUri(),
				uri.widthPx(),
				uri.heightPx()
			))
			.toList();

		placePhotoDomainService.saveAllIfNotExists(placePhotos);

		return PlacePhotoUrisResponse.from(uris);
	}

	private PlacePhotoUrisResponse toResponse(final List<PlacePhoto> placePhotos) {
		List<PlacePhotoUrisResponse.PhotoUri> photoUris = placePhotos.stream()
			.map(photo -> PlacePhotoUrisResponse.PhotoUri.of(
				photo.getPhotoName(),
				photo.getWidthPx(),
				photo.getHeightPx(),
				photo.getPhotoUri()
			))
			.toList();
		return PlacePhotoUrisResponse.from(photoUris);
	}

	private String fetchPhotoUri(final GooglePlaceDetailsResponse.PhotoMeta meta) {
		PlacePhotoRequest request = PlacePhotoRequest.of(
			meta.name(),
			meta.heightPx(),
			meta.widthPx()
		);

		return googleMapsPlacePhotoClient.getPhotoUri(request).uri();

	}
}
