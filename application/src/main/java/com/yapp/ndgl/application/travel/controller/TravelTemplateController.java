package com.yapp.ndgl.application.travel.controller;

import com.yapp.ndgl.application.travel.controller.dto.TravelTemplateResponse;
import com.yapp.ndgl.application.travel.facade.TravelTemplateFacade;
import com.yapp.ndgl.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TravelTemplateController implements TravelTemplateApi {

    private final TravelTemplateFacade travelTemplateFacade;

    @Override
    public ResponseEntity<SuccessResponse<TravelTemplateResponse>> getTravelTemplate(
        @PathVariable Long id
    ) {
        TravelTemplateResponse response = travelTemplateFacade.getTravelTemplate(id);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}
