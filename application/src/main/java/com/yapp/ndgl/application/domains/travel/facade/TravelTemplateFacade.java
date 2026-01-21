package com.yapp.ndgl.application.domains.travel.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateResponse;
import com.yapp.ndgl.application.domains.travel.service.TravelTemplateService;

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
        log.info("여행 템플릿의 상단 내역을 조회합니다 template id = {}", id);
        return travelTemplateService.readTravelTemplateHighlights(id);
    }
}
