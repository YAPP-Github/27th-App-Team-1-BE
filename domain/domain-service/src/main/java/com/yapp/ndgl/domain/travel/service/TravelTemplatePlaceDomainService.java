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

	@Transactional(readOnly = true)
	public List<TravelTemplatePlace> findPlacesByTravelTemplateIdAndDay(final Long travelTemplateId, final Integer day) {
		List<TravelTemplatePlaceEntity> placeEntities = travelTemplatePlaceRepository
			.findByTravelTemplateIdAndDayOrderBySequenceAsc(travelTemplateId, day);

		return placeEntities.stream()
			.map(this::toDomain)
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
			.map(this::toEntity)
			.toList();
		travelTemplatePlaceRepository.saveAll(entities);
	}

	private TravelTemplatePlace toDomain(final TravelTemplatePlaceEntity entity) {
		return TravelTemplatePlace.createWithId(
			entity.getId(),
			entity.getTravelTemplateId(),
			entity.getPlaceId(),
			entity.getSequence(),
			entity.getDay(),
			entity.getDistanceKm(),
			entity.getTransportationJson(),
			entity.getYoutubeTipsJson(),
			entity.getPlanBJson(),
			entity.getEstimatedDuration()
		);
	}

	private TravelTemplatePlaceEntity toEntity(final TravelTemplatePlace templatePlace) {
		return TravelTemplatePlaceEntity.builder()
			.travelTemplateId(templatePlace.getTravelTemplateId())
			.placeId(templatePlace.getPlaceId())
			.sequence(templatePlace.getSequence())
			.day(templatePlace.getDay())
			.distanceKm(templatePlace.getDistanceKm())
			.transportationJson(templatePlace.getTransportationJson())
			.youtubeTipsJson(templatePlace.getYoutubeTipsJson())
			.planBJson(templatePlace.getPlanBJson())
			.estimatedDuration(templatePlace.getEstimatedDuration())
			.build();
	}
}
