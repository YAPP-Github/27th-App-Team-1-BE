package com.yapp.ndgl.application.domains.travel.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplatePopularResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateRecommendationResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateSearchResponse;
import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.facade.TravelTemplateFacade;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.common.response.SuccessResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/travel-templates")
@RestController
public class TravelTemplateController implements TravelTemplateApi {

    private final TravelTemplateFacade travelTemplateFacade;

    @Override
    @PostMapping
    public ResponseEntity<SuccessResponse<Map<String, Long>>> saveTravelTemplate(
        @Valid @RequestBody final SaveTravelTemplateRequest request
    ) {
        Long templateId = travelTemplateFacade.saveTravelTemplate(request);
        return ResponseEntity.ok(SuccessResponse.success("id", templateId));
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

    @Override
    @GetMapping("/popular")
    public ResponseEntity<SuccessResponse<SliceResponse<TravelTemplatePopularResponse>>> readPopularTravelTemplates(
        @RequestParam(value = "travelProgramId", required = false) final Long travelProgramId,
        @RequestParam(value = "page", defaultValue = "0") final int page,
        @RequestParam(value = "size", defaultValue = "20") final int size
    ) {
        SliceResponse<TravelTemplatePopularResponse> response =
            travelTemplateFacade.readPopularTravelTemplates(travelProgramId, page, size);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Override
    @GetMapping("/recommend")
    public ResponseEntity<SuccessResponse<SliceResponse<TravelTemplateRecommendationResponse>>> readRecommendedTravelTemplates(
        @CurrentUuid String uuid,
        @RequestParam(value = "page", defaultValue = "0") final int page,
        @RequestParam(value = "size", defaultValue = "20") final int size
    ) {
        SliceResponse<TravelTemplateRecommendationResponse> response =
            travelTemplateFacade.readRecommendedTravelTemplates(uuid, page, size);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<SliceResponse<TravelTemplateSearchResponse>>> searchTravelTemplates(
        @RequestParam(value = "keyword") final String keyword,
        @RequestParam(value = "page", defaultValue = "0") final int page,
        @RequestParam(value = "size", defaultValue = "20") final int size
    ) {
        SliceResponse<TravelTemplateSearchResponse> response =
            travelTemplateFacade.searchTravelTemplates(keyword, page, size);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}
