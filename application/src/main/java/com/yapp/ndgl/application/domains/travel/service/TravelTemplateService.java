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
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplatePopularResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateSearchResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateRecommendationResponse;
import com.yapp.ndgl.application.domains.travel.event.TravelTemplateViewCountEvent;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.service.TravelTemplatePlaceDomainService;
import com.yapp.ndgl.domain.travel.service.UserTravelDomainService;
import com.yapp.ndgl.domain.user.service.UserDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelTemplateService {

    private final TravelTemplatePlaceDomainService travelTemplatePlaceDomainService;
    private final TravelTemplateDomainService travelTemplateDomainService;
    private final PlaceDomainService placeDomainService;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final UserDomainService userDomainService;
    private final UserTravelDomainService userTravelDomainService;

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

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplatePopularResponse> readPopularTravelTemplates(
        final Long travelProgramId,
        final int page,
        final int size
    ) {
        SliceResponse<TravelTemplate> templates =
            travelTemplateDomainService.findPopularTemplates(travelProgramId, page, size);
        List<TravelTemplatePopularResponse> content = templates.getContent().stream()
            .map(TravelTemplatePopularResponse::from)
            .toList();
        return SliceResponse.of(content, templates.isHasNext());
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplateRecommendationResponse> readRecommendedTravelTemplates(
        final String uuid, final int page, final int size
    ) {
        Long userId = userDomainService.findByUuid(uuid).getId();
        UserTravel latestUserTravel = userTravelDomainService.findLatestUpcomingByUserId(userId).orElse(null);
        String country = latestUserTravel == null ? null : latestUserTravel.getCountry();

        SliceResponse<TravelTemplate> templates = travelTemplateDomainService.findRecommendedTemplates(country, page, size);
        List<TravelTemplateRecommendationResponse> content = templates.getContent().stream()
            .map(TravelTemplateRecommendationResponse::from)
            .toList();
        return SliceResponse.of(content, templates.isHasNext());
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplateSearchResponse> searchTravelTemplates(
        final String keyword, final int page, final int size
    ) {
        SliceResponse<TravelTemplate> templates = travelTemplateDomainService.findByKeyword(keyword, page, size);
        List<TravelTemplateSearchResponse> content = templates.getContent().stream()
            .map(TravelTemplateSearchResponse::from)
            .toList();
        return SliceResponse.of(content, templates.isHasNext());
    }

}
