package com.yapp.ndgl.application.domains.place.controller;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yapp.ndgl.application.domains.place.api.PlaceApi;
import com.yapp.ndgl.application.domains.place.controller.request.SearchPlaceRequest;
import com.yapp.ndgl.application.domains.place.controller.response.PlaceDetailResponse;
import com.yapp.ndgl.application.domains.place.controller.response.PlaceFavoriteListResponse;
import com.yapp.ndgl.application.domains.place.controller.response.PlacePhotoResponse;
import com.yapp.ndgl.application.domains.place.facade.PlaceDetailFacade;
import com.yapp.ndgl.application.domains.place.facade.PlaceFavoriteFacade;
import com.yapp.ndgl.application.domains.place.facade.PlacePhotoFacade;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.common.response.SuccessResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/places")
public class PlaceController implements PlaceApi {

	private final PlaceDetailFacade placeDetailFacade;
	private final PlacePhotoFacade placePhotoFacade;
	private final PlaceFavoriteFacade placeFavoriteFacade;

	@Override
	@GetMapping("/detail")
	public ResponseEntity<?> getPlaceDetail(
		final @RequestParam("googlePlaceId") String googlePlaceId
	) {
		PlaceDetailResponse response = placeDetailFacade.getPlaceDetailFromDb(googlePlaceId);
		return ResponseEntity.ok(SuccessResponse.success("place", response));
	}

	@Override
	@PostMapping
	public ResponseEntity<?> searchAndSavePlace(
		final @Valid @RequestBody SearchPlaceRequest request
	) {
		PlaceDetailResponse response = placeDetailFacade.searchAndSavePlace(request.googlePlaceId());
		return ResponseEntity.ok(SuccessResponse.success("place", response));
	}

	@Override
	@GetMapping("/photos")
	public ResponseEntity<?> readPlacePhotos(
		final @RequestParam("googlePlaceId") String googlePlaceId
	) {
		PlacePhotoResponse response = placePhotoFacade.readPlacePhotos(googlePlaceId);
		return ResponseEntity.ok(SuccessResponse.success(response));
	}

	@Override
	@GetMapping("/favorite")
	public ResponseEntity<SuccessResponse<SliceResponse<PlaceFavoriteListResponse>>> readFavoritePlaces(
		@CurrentUuid String uuid,
		@RequestParam(value = "page", defaultValue = "0") final int page,
		@RequestParam(value = "size", defaultValue = "20") final int size
	) {
		SliceResponse<PlaceFavoriteListResponse> response = placeFavoriteFacade.readFavoritePlaces(uuid, page, size);
		return ResponseEntity.ok(SuccessResponse.success(response));
	}

	@Override
	@PostMapping("/favorite")
	public ResponseEntity<SuccessResponse> addFavoritePlace(
		@CurrentUuid String uuid,
		@RequestParam("googlePlaceId") final String googlePlaceId
	) {
		placeFavoriteFacade.addFavoritePlace(uuid, googlePlaceId);
		return ResponseEntity.ok(SuccessResponse.noContent());
	}

	@Override
	@DeleteMapping("/favorite")
	public ResponseEntity<SuccessResponse> removeFavoritePlace(
		@CurrentUuid String uuid,
		@RequestParam("googlePlaceId") final String googlePlaceId
	) {
		placeFavoriteFacade.removeFavoritePlace(uuid, googlePlaceId);
		return ResponseEntity.ok(SuccessResponse.noContent());
	}
}
