package com.yapp.ndgl.application.domains.travel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yapp.ndgl.application.domains.travel.controller.dto.TravelProgramResponse;
import com.yapp.ndgl.application.domains.travel.facade.TravelProgramFacade;
import com.yapp.ndgl.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/travel-programs")
@RestController
public class TravelProgramController implements TravelProgramApi {

    private final TravelProgramFacade travelProgramFacade;

    @Override
    @GetMapping
    public ResponseEntity<SuccessResponse<List<TravelProgramResponse>>> readTravelPrograms() {
        List<TravelProgramResponse> response = travelProgramFacade.readTravelPrograms();
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}
