package com.yapp.ndgl.application.domains.travel.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplatePopularResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateRecommendationResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateSearchResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateResponse;
import com.yapp.ndgl.application.domains.travel.service.TravelTemplateService;
import com.yapp.ndgl.common.response.SliceResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Facade
@RequiredArgsConstructor
public class TravelTemplateFacade {

    private final TravelTemplateService travelTemplateService;

    public TravelTemplateResponse getTravelTemplate(Long id) {
        return travelTemplateService.getTravelTemplate(id);
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
