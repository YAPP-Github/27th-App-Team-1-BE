package com.yapp.ndgl.application.domains.travel.facade;

import java.util.Map;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplatePopularResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateRecommendationResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateSearchResponse;
import com.yapp.ndgl.application.domains.travel.service.TravelTemplateSaveService;
import com.yapp.ndgl.application.domains.travel.service.TravelTemplateService;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.domain.place.Place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Facade
@RequiredArgsConstructor
public class TravelTemplateFacade {

    private final TravelTemplateService travelTemplateService;
    private final TravelTemplateSaveService travelTemplateSaveService;

    public Long saveTravelTemplate(final SaveTravelTemplateRequest request) {
        log.info("여행 템플릿을 저장합니다. traveler = {}", request.traveler());

        // Phase 1: 외부 API 호출 (트랜잭션 없음 — DB 커넥션 점유하지 않음)
        Map<String, Place> resolvedPlaces = travelTemplateService.resolveAllPlaces(request);

        // Phase 2: DB 저장 (트랜잭션)
        return travelTemplateSaveService.persistTravelTemplate(request, resolvedPlaces);
    }

    public TravelTemplateHighlightsResponse readTravelTemplateHighlights(final Long id) {
        log.info("여행 템플릿의 상단 내역을 조회합니다. templateId = {}", id);
        return travelTemplateService.readTravelTemplateHighlights(id);
    }

    public TravelTemplateItineraryResponse readTravelTemplateItinerary(final Long id, final Integer day) {
        log.info("여행 템플릿의 일정을 조회합니다. templateId = {}", id);
        return travelTemplateService.readTravelTemplateItinerary(id, day);
    }

    public SliceResponse<TravelTemplatePopularResponse> readPopularTravelTemplates(
        final Long travelProgramId, final int page, final int size
    ) {
        log.info("인기 여행 템플릿 목록을 조회합니다. travelProgramId = {}, page = {}, size = {}", travelProgramId, page, size);
        return travelTemplateService.readPopularTravelTemplates(travelProgramId, page, size);
    }

    public SliceResponse<TravelTemplateRecommendationResponse> readRecommendedTravelTemplates(
        final String uuid, final int page, final int size
    ) {
        log.info("추천 여행 템플릿 목록을 조회합니다. uuid = {}, page = {}, size = {}", uuid, page, size);
        return travelTemplateService.readRecommendedTravelTemplates(uuid, page, size);
    }

    public SliceResponse<TravelTemplateSearchResponse> searchTravelTemplates(
        final String keyword, final int page, final int size
    ) {
        log.info("여행 템플릿 검색을 수행합니다. keyword = {}, page = {}, size = {}", keyword, page, size);
        return travelTemplateService.searchTravelTemplates(keyword, page, size);
    }
}
