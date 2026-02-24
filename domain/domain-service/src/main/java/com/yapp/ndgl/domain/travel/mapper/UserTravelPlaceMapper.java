package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.UserTravelPlace;
import com.yapp.ndgl.domain.travel.entity.UserTravelPlaceEntity;

public class UserTravelPlaceMapper {

	public static UserTravelPlaceEntity toEntity(final UserTravelPlace userTravelPlace) {
		if (userTravelPlace == null) {
			return null;
		}

		return UserTravelPlaceEntity.builder()
			.userTravelId(userTravelPlace.getUserTravelId())
			.placeId(userTravelPlace.getPlaceId())
			.day(userTravelPlace.getDay())
			.sequence(userTravelPlace.getSequence())
			.memo(userTravelPlace.getMemo())
			.distanceKm(userTravelPlace.getDistanceKm())
			.transportationJson(userTravelPlace.getTransportationJson())
			.startTime(userTravelPlace.getStartTime())
			.estimatedDuration(userTravelPlace.getEstimatedDuration())
			.budget(userTravelPlace.getBudget())
			.build();
	}

	public static UserTravelPlace toDomain(final UserTravelPlaceEntity entity) {
		if (entity == null) {
			return null;
		}

		return UserTravelPlace.builder()
			.id(entity.getId())
			.userTravelId(entity.getUserTravelId())
			.placeId(entity.getPlaceId())
			.day(entity.getDay())
			.sequence(entity.getSequence())
			.memo(entity.getMemo())
			.distanceKm(entity.getDistanceKm())
			.transportationJson(entity.getTransportationJson())
			.startTime(entity.getStartTime())
			.estimatedDuration(entity.getEstimatedDuration())
			.budget(entity.getBudget())
			.build();
	}
}
