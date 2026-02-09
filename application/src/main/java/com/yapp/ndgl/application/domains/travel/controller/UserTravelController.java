package com.yapp.ndgl.application.domains.travel.controller;

import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.facade.UserTravelFacade;
import com.yapp.ndgl.common.response.SuccessResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/travels")
@RestController
public class UserTravelController implements UserTravelApi {

    private final UserTravelFacade userTravelFacade;

    @Override
    @PostMapping
    public ResponseEntity<?> createUserTravel(
        @CurrentUuid String uuid,
        @Valid @RequestBody CreateUserTravelRequest request
    ) {
        Long userTravelId = userTravelFacade.createUserTravel(uuid, request);
        return ResponseEntity.ok(SuccessResponse.success("userTravelId", userTravelId));
    }

    @Override
    @GetMapping("/upcoming")
    public ResponseEntity<UpcomingUserTravelResponse> getUpcomingUserTravel(
        @CurrentUuid String uuid
    ) {
        UpcomingUserTravelResponse response = userTravelFacade.getUpcomingUserTravel(uuid);
        return ResponseEntity.ok(response);
    }
}
