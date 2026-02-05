package com.yapp.ndgl.application.domains.travel.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateResponse;
import com.yapp.ndgl.application.domains.travel.event.TravelTemplateViewCountEvent;
import com.yapp.ndgl.common.type.TransportationMode;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.service.TravelTemplatePlaceDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelTemplateService {

    private final TravelTemplatePlaceDomainService travelTemplatePlaceDomainService;
    private final TravelTemplateDomainService travelTemplateDomainService;
    private final PlaceDomainService placeDomainService;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public TravelTemplateResponse getTravelTemplate(Long id) {
        // 템플릿 조회
        TravelTemplate travelTemplate = travelTemplateDomainService.findById(id);

        // 매핑 테이블 조회
        List<TravelTemplatePlace> travelTemplatePlaces = travelTemplateDomainService
            .findPlacesByTravelTemplateId(id);

        List<Long> placeIds = travelTemplatePlaces.stream()
            .map(TravelTemplatePlace::getPlaceId)
            .collect(Collectors.toList());

        // 장소 목록 조회
        List<Place> placeList = placeDomainService.findByIds(placeIds);
        Map<Long, Place> placeMap = placeList.stream()
            .collect(Collectors.toMap(Place::getId, place -> place));

        List<TravelTemplateResponse.TravelTemplatePlaceResponse> places = travelTemplatePlaces.stream()
            .map(travelTemplatePlace -> {
                Place place = placeMap.get(travelTemplatePlace.getPlaceId());

                // youtubeTipsJson 파싱
                String travelerTip = null;
                List<String> youtubeTips = null;
                if (travelTemplatePlace.getYoutubeTipsJson() != null) {
                    try {
                        List<String> tips = objectMapper.readValue(
                            travelTemplatePlace.getYoutubeTipsJson(),
                            new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {}
                        );
                        youtubeTips = tips;
                        travelerTip = tips.isEmpty() ? null : tips.get(0);
                    } catch (Exception e) {
                        // JSON 파싱 실패 시 null
                    }
                }

                // transportationJson 파싱
                List<TravelTemplateResponse.TransportationInfo> transportation = null;
                if (travelTemplatePlace.getTransportationJson() != null) {
                    try {
                        List<java.util.Map<String, Object>> transportList = objectMapper.readValue(
                            travelTemplatePlace.getTransportationJson(),
                            new com.fasterxml.jackson.core.type.TypeReference<List<java.util.Map<String, Object>>>() {}
                        );
                        transportation = transportList.stream()
                            .map(t -> {
                                String modeStr = (String) t.get("mode");
                                TransportationMode mode = modeStr != null
                                    ? TransportationMode.valueOf(modeStr)
                                    : null;
                                Integer timeMin = t.get("time_min") != null ? ((Number) t.get("time_min")).intValue() : null;
                                return new TravelTemplateResponse.TransportationInfo(mode, timeMin);
                            })
                            .toList();
                    } catch (Exception e) {
                        // JSON 파싱 실패 시 null
                    }
                }

                // planBJson 파싱
                List<TravelTemplateResponse.PlanBInfo> planB = null;
                if (travelTemplatePlace.getPlanBJson() != null) {
                    try {
                        List<java.util.Map<String, String>> planBList = objectMapper.readValue(
                            travelTemplatePlace.getPlanBJson(),
                            new com.fasterxml.jackson.core.type.TypeReference<List<java.util.Map<String, String>>>() {}
                        );
                        planB = planBList.stream()
                            .map(p -> new TravelTemplateResponse.PlanBInfo(
                                p.get("name"),
                                p.get("feature")
                            ))
                            .toList();
                    } catch (Exception e) {
                        // JSON 파싱 실패 시 null
                    }
                }

                return new TravelTemplateResponse.TravelTemplatePlaceResponse(
                    travelTemplatePlace.getSequence(),
                    travelTemplatePlace.getDay(),
                    travelTemplatePlace.getDistanceKm(),
                    transportation,
                    travelerTip,
                    youtubeTips,
                    planB,
                    place == null ? null : new TravelTemplateResponse.PlaceResponse(
                        place.getGooglePlaceId(),
                        place.getFormattedAddress(),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getRating(),
                        place.getNationalPhoneNumber(),
                        place.getInternationalPhoneNumber(),
                        place.getWebsiteUri(),
                        place.getGoogleMapsUri(),
                        place.getUserRatingCount()
                    )
                );
            })
            .collect(Collectors.toList());

        return new TravelTemplateResponse(
            travelTemplate.getTravelId(),
            travelTemplate.getTraveler(),
            travelTemplate.getCountry(),
            travelTemplate.getCity(),
            travelTemplate.getWeatherInfo(),
            travelTemplate.getCultureInfo(),
            travelTemplate.getFoodInfo(),
            travelTemplate.getThumbnail(),
            travelTemplate.getLink(),
            travelTemplate.getBudgetPerPerson(),
            travelTemplate.getSummary(),
            travelTemplate.getTitle(),
            travelTemplate.getNights(),
            travelTemplate.getDays(),
            places
        );
    }

    @Transactional(readOnly = true)
    public TravelTemplateHighlightsResponse readTravelTemplateHighlights(final Long id) {

        // 템플릿 상단에 보여줄 핵심 요약 묶음
        TravelTemplate travelTemplate = travelTemplateDomainService.findById(id);

        // 조회수 증가 이벤트 발행
        eventPublisher.publishEvent(new TravelTemplateViewCountEvent(id));

        return TravelTemplateHighlightsResponse.toResponse(travelTemplate);
    }

    @Transactional(readOnly = true)
    public TravelTemplateItineraryResponse readTravelTemplateItinerary(final Long travelTemplateId, final Integer day) {
        // 여행 템플릿 장소 목록 조회 (day 파라미터에 따라 DB에서 필터링)
        List<TravelTemplatePlace> travelTemplatePlaces = travelTemplatePlaceDomainService.findPlacesByTravelTemplateIdAndDay(travelTemplateId, day);

        // placeId 목록 추출
        List<Long> placeIds = travelTemplatePlaces.stream()
            .map(TravelTemplatePlace::getPlaceId)
            .collect(Collectors.toList());

        // 장소 목록 조회
        List<Place> placeList = placeDomainService.findByIds(placeIds);
        Map<Long, Place> placeMap = placeList.stream()
            .collect(Collectors.toMap(Place::getId, place -> place));

        return TravelTemplateItineraryResponse.of(travelTemplatePlaces, placeMap, objectMapper);
    }

}
