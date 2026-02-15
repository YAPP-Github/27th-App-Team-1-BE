package com.yapp.ndgl.application.domains.travel.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.travel.TravelProgram;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.service.TravelProgramDomainService;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.service.TravelTemplatePlaceDomainService;
import com.yapp.ndgl.domain.travel.type.TravelProgramType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelTemplateSaveService {

	private final PlaceDomainService placeDomainService;
	private final TravelProgramDomainService travelProgramDomainService;
	private final TravelTemplateDomainService travelTemplateDomainService;
	private final TravelTemplatePlaceDomainService travelTemplatePlaceDomainService;
	private final ObjectMapper objectMapper;

	/**
	 * Phase 2: 수집된 장소 데이터를 기반으로 DB에 저장한다. (트랜잭션)
	 */
	@Transactional
	public Long persistTravelTemplate(final SaveTravelTemplateRequest request, final Map<String, Place> resolvedPlaces) {
		// 1. 아직 저장되지 않은 Place를 DB에 저장
		Map<String, Place> savedPlaces = new HashMap<>();
		for (Map.Entry<String, Place> entry : resolvedPlaces.entrySet()) {
			Place place = entry.getValue();
			if (place.getId() == null) {
				place = placeDomainService.save(place);
			}
			savedPlaces.put(entry.getKey(), place);
		}

		// 2. TravelProgram 조회 또는 생성
		TravelProgram travelProgram = travelProgramDomainService.findByName(request.traveler())
			.orElseGet(() -> travelProgramDomainService.createTravelProgram(request.traveler(), null, TravelProgramType.YOUTUBE));

		// 3. TravelTemplate 저장
		int days = request.itinerary().size();
		int nights = Math.max(days - 1, 0);

		TravelTemplate travelTemplate = TravelTemplate.create(
			request.traveler(),
			"여행 프로그램 이름",
			"유튜버 이미지",
			TravelProgramType.YOUTUBE,
			request.country(),
			request.city(),
			request.summary(),
			"title",
			request.budgetPerPerson(),
			nights,
			days
		);

		TravelTemplate savedTemplate = travelTemplateDomainService.createTravelTemplate(travelTemplate, travelProgram);
		log.info("여행 템플릿 저장 완료. templateId = {}", savedTemplate.getId());

		// 4. TravelTemplatePlace 일괄 저장
		List<TravelTemplatePlace> templatePlaces = new ArrayList<>();

		for (SaveTravelTemplateRequest.ItineraryRequest itinerary : request.itinerary()) {
			for (SaveTravelTemplateRequest.ActivityRequest activity : itinerary.activities()) {
				Place place = savedPlaces.get(activity.placeName());
				String planBJson = buildPlanBJson(activity.planB(), savedPlaces);

				TravelTemplatePlace templatePlace = TravelTemplatePlace.create(
					savedTemplate.getId(),
					place.getId(),
					activity.sequence(),
					itinerary.day(),
					activity.distanceKm(),
					serializeToJson(activity.transportation()),
					serializeToJson(activity.travelerTips()),
					planBJson,
					activity.estimatedTime()
				);
				templatePlaces.add(templatePlace);
			}
		}

		travelTemplatePlaceDomainService.saveAllFromDomain(templatePlaces);
		log.info("여행 템플릿 장소 저장 완료. templateId = {}, 장소 수 = {}", savedTemplate.getId(), templatePlaces.size());

		return savedTemplate.getId();
	}

	private String buildPlanBJson(final List<SaveTravelTemplateRequest.PlanBRequest> planBList,
		final Map<String, Place> savedPlaces) {
		if (planBList == null || planBList.isEmpty()) {
			return null;
		}

		List<PlanBInfo> planBInfos = planBList.stream()
			.map(planB -> new PlanBInfo(savedPlaces.get(planB.name()).getId(), planB.name(), planB.feature()))
			.toList();

		return serializeToJson(planBInfos);
	}

	private String serializeToJson(final Object value) {
		if (value == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			log.error("JSON 직렬화 실패", e);
			throw new GlobalException(GoogleMapsErrorCode.RESPONSE_PARSE_FAILED);
		}
	}

	private record PlanBInfo(Long placeId, String name, String feature) {}
}
