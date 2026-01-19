package com.yapp.ndgl.domain.place.mapper;

import com.yapp.ndgl.domain.place.PlacePhoto;
import com.yapp.ndgl.domain.place.entity.PlacePhotoEntity;

public class PlacePhotoMapper {

	public static PlacePhotoEntity toEntity(final PlacePhoto placePhoto) {
		return PlacePhotoEntity.builder()
			.placeId(placePhoto.getPlaceId())
			.photoName(placePhoto.getPhotoName())
			.photoUri(placePhoto.getPhotoUri())
			.widthPx(placePhoto.getWidthPx())
			.heightPx(placePhoto.getHeightPx())
			.build();
	}

	public static PlacePhoto toDomain(final PlacePhotoEntity entity) {
		return PlacePhoto.builder()
			.id(entity.getId())
			.placeId(entity.getPlaceId())
			.photoName(entity.getPhotoName())
			.photoUri(entity.getPhotoUri())
			.widthPx(entity.getWidthPx())
			.heightPx(entity.getHeightPx())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}
}
