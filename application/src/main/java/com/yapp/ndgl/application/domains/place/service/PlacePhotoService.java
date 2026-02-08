package com.yapp.ndgl.application.domains.place.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.place.controller.response.PlacePhotoResponse;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.PlacePhoto;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.place.service.PlacePhotoDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlacePhotoService {

	private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;
	private final PlacePhotoDomainService placePhotoDomainService;
	private final PlaceDomainService placeDomainService;
	private final ObjectMapper objectMapper;

	/**
	 * googlePlaceId에 해당하는 장소의 사진들을 비동기로 DB에 저장한다.
	 * 이미 저장된 사진은 제외하고, 없는 사진만 Google API를 호출하여 저장한다.
	 *
	 * 비동기 처리 이유:
	 * - Photo API 호출은 여러 번 발생할 수 있어 시간이 오래 걸림 (사진 개수만큼)
	 * - 메인 응답(장소 상세 정보)에 필수가 아님
	 * - 백그라운드에서 처리하여 API 응답 속도 개선
	 *
	 * @param googlePlaceId 장소 ID
	 */
	@Async("photoAsyncExecutor")
	public void savePhotosIfNotExists(final String googlePlaceId) {
		log.info("사진 저장 시작. googlePlaceId={}", googlePlaceId);

		try {
			// 1. Place 조회하여 photosJson 파싱
			Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);
			if (place.getPhotosJson() == null || place.getPhotosJson().isEmpty()) {
				return;
			}

			List<PhotoMeta> photoMetas = objectMapper.readValue(
				place.getPhotosJson(),
				new TypeReference<>() {}
			);

			if (photoMetas == null || photoMetas.isEmpty()) {
				return;
			}

			// 2. DB에서 기존 photo 조회
			List<PlacePhoto> existingPhotos = placePhotoDomainService.findByGooglePlaceId(googlePlaceId);
			Map<String, PlacePhoto> existingPhotoMap = existingPhotos.stream()
				.collect(Collectors.toMap(PlacePhoto::getPhotoName, p -> p));

			// 3. photoMetas를 순회하면서 없는 것만 API 호출
			List<PlacePhoto> newPhotos = new ArrayList<>();
			for (PhotoMeta photoMeta : photoMetas) {
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

			// 4. 새로운 photo만 저장
			if (!newPhotos.isEmpty()) {
				placePhotoDomainService.saveAllIfNotExists(newPhotos);
				log.info("{}개의 새로운 사진 저장 완료. googlePlaceId={}", newPhotos.size(), googlePlaceId);
			}

			log.info("사진 저장 완료. googlePlaceId={}", googlePlaceId);
		} catch (Exception e) {
			// 비동기 처리이므로 예외를 던지지 않고 로그만 남김
			// 사진 저장 실패가 메인 응답에 영향을 주지 않도록 함
			log.error("사진 비동기 저장 중 오류 발생. googlePlaceId={}. 사진은 나중에 별도 조회 가능.",
				googlePlaceId, e);
		}
	}

	public PlacePhotoResponse readPlacePhotoUris(final String googlePlaceId) {
		List<PlacePhoto> photos = placePhotoDomainService.findByGooglePlaceId(googlePlaceId);
		if (photos.isEmpty()) {
			return PlacePhotoResponse.empty();
		}

		return PlacePhotoResponse.toResponse(photos);
	}

	private String fetchPhotoUri(final PhotoMeta meta) {
		PlacePhotoRequest request = PlacePhotoRequest.of(
			meta.name(),
			meta.heightPx(),
			meta.widthPx()
		);

		return googleMapsPlacePhotoClient.getPhotoUri(request).uri();
	}

	/**
	 * Photo 메타데이터 내부 DTO
	 */
	private record PhotoMeta(
		String name,
		Integer widthPx,
		Integer heightPx
	) {
	}
}
