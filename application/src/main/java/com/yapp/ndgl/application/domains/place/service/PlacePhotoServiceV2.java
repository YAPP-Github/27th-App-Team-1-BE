package com.yapp.ndgl.application.domains.place.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.common.executor.CompletableFutureExecutor;
import com.yapp.ndgl.application.domains.place.controller.response.PlacePhotoResponse;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.PlacePhoto;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.place.service.PlacePhotoDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlacePhotoServiceV2 {

	private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;
	private final PlacePhotoDomainService placePhotoDomainService;
	private final PlaceDomainService placeDomainService;
	private final ObjectMapper objectMapper;

	public void savePhotosIfNotExists(final String googlePlaceId) {
		log.info("사진 저장 시작. googlePlaceId={}", googlePlaceId);

		Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);
		List<PhotoMeta> photoMetas = parsePhotosJson(place);
		if (photoMetas.isEmpty()) {
			return;
		}

		Map<String, PlacePhoto> existingPhotoMap = placePhotoDomainService.findByGooglePlaceId(googlePlaceId)
			.stream()
			.collect(Collectors.toMap(PlacePhoto::getPhotoName, p -> p));

		List<PhotoMeta> targets = photoMetas.stream()
			.filter(meta -> !existingPhotoMap.containsKey(meta.name()))
			.toList();

		List<PlacePhoto> newPhotos = parallelFetch(googlePlaceId, targets);

		if (!newPhotos.isEmpty()) {
			placePhotoDomainService.saveAllIfNotExists(newPhotos);
			log.info("{}개의 새로운 사진 저장 완료. googlePlaceId={}", newPhotos.size(), googlePlaceId);
		}

		log.info("사진 저장 완료. googlePlaceId={}", googlePlaceId);
	}

	public PlacePhotoResponse readPlacePhotoUris(final String googlePlaceId) {
		Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);

		List<PlacePhoto> existingPhotos = placePhotoDomainService.findByGooglePlaceId(googlePlaceId);
		List<PhotoMeta> target = buildPhotoMeta(place, existingPhotos);
		List<PlacePhoto> newPhotos = parallelFetch(googlePlaceId, target);

		placePhotoDomainService.saveAll(newPhotos);

		List<PlacePhoto> allPhotos = new ArrayList<>(existingPhotos);
		allPhotos.addAll(newPhotos);

		if (allPhotos.isEmpty()) {
			return PlacePhotoResponse.empty();
		}

		return PlacePhotoResponse.toResponse(allPhotos);
	}

	private List<PhotoMeta> buildPhotoMeta(final Place place,
		final List<PlacePhoto> existingPhotos) {

		List<PhotoMeta> photoMetas = parsePhotosJson(place);
		if (photoMetas.isEmpty()) {
			return List.of();
		}

		Map<String, PlacePhoto> existingPhotoMap = existingPhotos.stream()
			.collect(Collectors.toMap(PlacePhoto::getPhotoName, p -> p));

		return photoMetas.stream()
			.filter(meta -> !existingPhotoMap.containsKey(meta.name()))
			.toList();

	}

	private List<PlacePhoto> parallelFetch(final String googlePlaceId, final List<PhotoMeta> targets) {
		List<CompletableFuture<PlacePhoto>> futures = targets.stream()
			.flatMap(meta -> CompletableFutureExecutor.invoke(
				() -> fetch(googlePlaceId, meta)).stream())
			.toList();

		return futures.stream()
			.map(CompletableFuture::join)
			.filter(Objects::nonNull)
			.toList();
	}

	private List<PhotoMeta> parsePhotosJson(final Place place) {
		try {
			if (place.getPhotosJson() == null || place.getPhotosJson().isEmpty()) {
				return List.of();
			}
			List<PhotoMeta> photoMetas = objectMapper.readValue(place.getPhotosJson(), new TypeReference<>() {
			});
			return photoMetas == null ? List.of() : photoMetas;
		} catch (Exception e) {
			log.error("photos json 파싱중 에러가 발생하였습니다. {}", e.getMessage());
			throw new GlobalException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private PlacePhoto fetch(final String googlePlaceId, final PhotoMeta meta) {
		PlacePhotoRequest request = PlacePhotoRequest.of(meta.name(), meta.heightPx(), meta.widthPx());
		String uri = googleMapsPlacePhotoClient.getPhotoUri(request).uri();
		return PlacePhoto.create(googlePlaceId, meta.name(), uri, meta.widthPx(), meta.heightPx());
	}

	private record PhotoMeta(
		String name,
		Integer widthPx,
		Integer heightPx
	) {
	}
}
