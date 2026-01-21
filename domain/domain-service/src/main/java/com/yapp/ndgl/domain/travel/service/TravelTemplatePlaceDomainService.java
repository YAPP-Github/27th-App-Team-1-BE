package com.yapp.ndgl.domain.travel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.entity.TravelTemplatePlaceEntity;
import com.yapp.ndgl.domain.travel.repository.TravelTemplatePlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelTemplatePlaceDomainService {

	private final TravelTemplatePlaceRepository travelTemplatePlaceRepository;

	public List<TravelTemplatePlace> findPlacesByTravelTemplateId(final Long travelTemplateId) {
		List<TravelTemplatePlaceEntity> placeEntities = travelTemplatePlaceRepository
			.findByTravelTemplateIdOrderByDayAscSequenceAsc(travelTemplateId);

		return placeEntities.stream()
			.map(entity -> TravelTemplatePlace.builder()
				.id(entity.getId())
				.travelTemplateId(entity.getTravelTemplateId())
				.sequence(entity.getSequence())
				.day(entity.getDay())
				.travelerTip(entity.getTravelerTip())
				.placeId(entity.getPlaceId())
				.estimatedDuration(entity.getEstimatedDuration())
				.build())
			.collect(Collectors.toList());
	}

	public List<TravelTemplatePlace> findPlacesByTravelTemplateIdAndDay(final Long travelTemplateId, final Integer day) {
		List<TravelTemplatePlaceEntity> placeEntities = travelTemplatePlaceRepository
			.findByTravelTemplateIdAndDayOrderBySequenceAsc(travelTemplateId, day);

		return placeEntities.stream()
			.map(entity -> TravelTemplatePlace.builder()
				.id(entity.getId())
				.travelTemplateId(entity.getTravelTemplateId())
				.sequence(entity.getSequence())
				.day(entity.getDay())
				.travelerTip(entity.getTravelerTip())
				.placeId(entity.getPlaceId())
				.estimatedDuration(entity.getEstimatedDuration())
				.build())
			.collect(Collectors.toList());
	}
}
