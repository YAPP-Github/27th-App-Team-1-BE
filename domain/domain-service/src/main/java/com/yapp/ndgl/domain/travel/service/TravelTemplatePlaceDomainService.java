package com.yapp.ndgl.domain.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void saveAll(final List<TravelTemplatePlaceEntity> entities) {
		travelTemplatePlaceRepository.saveAll(entities);
	}

	/**
	 * 도메인 객체 리스트를 엔티티로 변환 후 일괄 저장한다.
	 */
	@Transactional
	public void saveAllFromDomain(final List<TravelTemplatePlace> templatePlaces) {
		List<TravelTemplatePlaceEntity> entities = templatePlaces.stream()
			.map(tp -> TravelTemplatePlaceEntity.builder()
				.travelTemplateId(tp.getTravelTemplateId())
				.placeId(tp.getPlaceId())
				.sequence(tp.getSequence())
				.day(tp.getDay())
				.distanceKm(tp.getDistanceKm())
				.transportationJson(tp.getTransportationJson())
				.youtubeTipsJson(tp.getYoutubeTipsJson())
				.planBJson(tp.getPlanBJson())
				.estimatedDuration(tp.getEstimatedDuration())
				.build())
			.toList();
		travelTemplatePlaceRepository.saveAll(entities);
	}
}
