package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateResponse;
import com.yapp.ndgl.application.domains.travel.facade.TravelTemplateFacade;
import com.yapp.ndgl.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/travel-templates")
@RestController
public class TravelTemplateController implements TravelTemplateApi {

    private final TravelTemplateFacade travelTemplateFacade;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<TravelTemplateResponse>> getTravelTemplate(
        @PathVariable Long id
    ) {
        TravelTemplateResponse response = travelTemplateFacade.getTravelTemplate(id);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @GetMapping("/{id}/content-card")
    public ResponseEntity<SuccessResponse<TravelTemplateHighlightsResponse>> readTravelTemplateHighlights(
        @PathVariable("id") final Long id
    ) {
        TravelTemplateHighlightsResponse response = travelTemplateFacade.readTravelTemplateHighlights(id);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Override
    @GetMapping("/{id}/itinerary")
    public ResponseEntity<SuccessResponse<TravelTemplateItineraryResponse>> readTravelTemplateItinerary(
        @PathVariable("id") final Long id,
        @RequestParam(value = "day", required = false) final Integer day
    ) {
        TravelTemplateItineraryResponse response = travelTemplateFacade.readTravelTemplateItinerary(id, day);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}
