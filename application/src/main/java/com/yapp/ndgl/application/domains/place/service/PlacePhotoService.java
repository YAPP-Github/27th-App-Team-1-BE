package com.yapp.ndgl.application.domains.place.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yapp.ndgl.application.domains.place.controller.response.PlacePhotoResponse;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.common.type.PhotoMeta;
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
			Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);
			List<PhotoMeta> photoMetas = place.getPhotos();
			if (photoMetas == null || photoMetas.isEmpty()) {
				return;
			}

			List<PlacePhoto> existingPhotos = placePhotoDomainService.findByGooglePlaceId(googlePlaceId);
			Map<String, PlacePhoto> existingPhotoMap = existingPhotos.stream()
				.collect(Collectors.toMap(PlacePhoto::getPhotoName, p -> p));

			List<PlacePhoto> newPhotos = new ArrayList<>();
			for (PhotoMeta photoMeta : photoMetas) {
				if (!existingPhotoMap.containsKey(photoMeta.name())) {
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

			if (!newPhotos.isEmpty()) {
				placePhotoDomainService.saveAllIfNotExists(newPhotos);
				log.info("{}개의 새로운 사진 저장 완료. googlePlaceId={}", newPhotos.size(), googlePlaceId);
			}

			log.info("사진 저장 완료. googlePlaceId={}", googlePlaceId);
		} catch (Exception e) {
			log.error("사진 비동기 저장 중 오류 발생. googlePlaceId={}. 사진은 나중에 별도 조회 가능.",
				googlePlaceId, e);
		}
	}

	/**
	 * googlePlaceId에 해당하는 장소의 사진 URI 목록을 조회한다.
	 * DB에 저장된 사진을 반환하되, photos에는 존재하지만 DB에는 없는 미저장 사진이 있으면
	 * Google API를 호출하여 조회 후 반환 및 DB에 저장한다.
	 *
	 * @param googlePlaceId 장소 ID
	 * @return 사진 URI 목록
	 */
	public PlacePhotoResponse readPlacePhotoUris(final String googlePlaceId) {
		Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);

		List<PlacePhoto> existingPhotos = placePhotoDomainService.findByGooglePlaceId(googlePlaceId);
		List<PlacePhoto> missingPhotos = fetchAndSaveMissingPhotos(googlePlaceId, place, existingPhotos);

		List<PlacePhoto> allPhotos = new ArrayList<>(existingPhotos);
		allPhotos.addAll(missingPhotos);

		if (allPhotos.isEmpty()) {
			return PlacePhotoResponse.empty();
		}

		return PlacePhotoResponse.toResponse(allPhotos);
	}

	/**
	 * photos에는 존재하지만 DB에는 없는 미저장 사진을 Google API로 조회 후 저장한다.
	 */
	private List<PlacePhoto> fetchAndSaveMissingPhotos(final String googlePlaceId, final Place place,
		final List<PlacePhoto> existingPhotos) {
		try {
			List<PhotoMeta> photoMetas = place.getPhotos();
			if (photoMetas == null || photoMetas.isEmpty()) {
				return List.of();
			}

			Map<String, PlacePhoto> existingPhotoMap = existingPhotos.stream()
				.collect(Collectors.toMap(PlacePhoto::getPhotoName, p -> p));

			List<PlacePhoto> newPhotos = new ArrayList<>();
			for (PhotoMeta photoMeta : photoMetas) {
				if (!existingPhotoMap.containsKey(photoMeta.name())) {
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

			if (!newPhotos.isEmpty()) {
				placePhotoDomainService.saveAllIfNotExists(newPhotos);
				log.info("사진 조회 시 {}개의 미저장 사진 저장 완료. googlePlaceId={}", newPhotos.size(), googlePlaceId);
			}

			return newPhotos;
		} catch (Exception e) {
			log.error("미저장 사진 조회 및 저장 중 오류 발생. googlePlaceId={}. 기존 저장된 사진만 반환합니다.",
				googlePlaceId, e);
			return List.of();
		}
	}

	private String fetchPhotoUri(final PhotoMeta meta) {
		PlacePhotoRequest request = PlacePhotoRequest.of(
			meta.name(),
			meta.heightPx(),
			meta.widthPx()
		);

		return googleMapsPlacePhotoClient.getPhotoUri(request).uri();
	}
}
