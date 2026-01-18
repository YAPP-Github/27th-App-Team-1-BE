package com.yapp.ndgl.application.travel.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.travel.controller.dto.TravelTemplateResponse;
import com.yapp.ndgl.application.travel.service.TravelTemplateService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class TravelTemplateFacade {

    private final TravelTemplateService travelTemplateService;

    public TravelTemplateResponse getTravelTemplate(Long id) {
        return travelTemplateService.getTravelTemplate(id);
    }
}
