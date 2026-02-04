package com.yapp.ndgl.domain.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.entity.TravelTemplatePlaceEntity;
import com.yapp.ndgl.domain.travel.repository.TravelTemplatePlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelTemplatePlaceDomainService {

	private final TravelTemplatePlaceRepository travelTemplatePlaceRepository;

	public List<TravelTemplatePlace> findPlacesByTravelTemplateIdAndDay(final Long travelTemplateId, final Integer day) {
		List<TravelTemplatePlaceEntity> placeEntities = travelTemplatePlaceRepository
			.findByTravelTemplateIdAndDayOrderBySequenceAsc(travelTemplateId, day);

		return placeEntities.stream()
			.map(entity -> TravelTemplatePlace.builder()
				.id(entity.getId())
				.travelTemplateId(entity.getTravelTemplateId())
				.sequence(entity.getSequence())
				.day(entity.getDay())
				.distanceKm(entity.getDistanceKm())
				.transportationJson(entity.getTransportationJson())
				.youtubeTipsJson(entity.getYoutubeTipsJson())
				.planBJson(entity.getPlanBJson())
				.placeId(entity.getPlaceId())
				.estimatedDuration(entity.getEstimatedDuration())
				.build())
			.toList();
	}
}
