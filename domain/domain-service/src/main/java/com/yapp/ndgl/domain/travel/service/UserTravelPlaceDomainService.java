package com.yapp.ndgl.domain.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravelPlace;
import com.yapp.ndgl.domain.travel.entity.UserTravelPlaceEntity;
import com.yapp.ndgl.domain.travel.repository.UserTravelPlaceRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserTravelPlaceDomainService {

	private final UserTravelPlaceRepository userTravelPlaceRepository;

	public void createUserTravelPlaces(final List<TravelTemplatePlace> templatePlaces, final Long userTemplateId) {

		List<UserTravelPlace> userTravelPlaces = templatePlaces.stream()
			.map(templatePlace -> UserTravelPlace.create(
				userTemplateId,
				templatePlace.getPlaceId(),
				templatePlace.getDay(),
				templatePlace.getSequence(),
				templatePlace.getTravelerTip(),
				templatePlace.getEstimatedDuration()
			))
			.toList();

		List<UserTravelPlaceEntity> entities = userTravelPlaces.stream()
			.map(place -> UserTravelPlaceEntity.builder()
				.userTravelId(place.getUserTravelId())
				.placeId(place.getPlaceId())
				.day(place.getDay())
				.sequence(place.getSequence())
				.travelerTip(place.getTravelerTip())
				.estimatedDuration(place.getEstimatedDuration())
				.build())
			.toList();

		userTravelPlaceRepository.saveAll(entities);
	}
}
