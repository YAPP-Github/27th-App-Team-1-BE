package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelPlaceRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.ReplaceUserTravelItineraryRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpdateUserTravelPlaceRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpdateUserTravelPlaceStartTimesRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpdateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelListResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelContentCardResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelItineraryResponse;
import com.yapp.ndgl.application.domains.travel.facade.UserTravelFacade;
import com.yapp.ndgl.common.response.SliceResponse;
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
    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateUserTravel(
        @CurrentUuid String uuid,
        @PathVariable("id") final Long id,
        @Valid @RequestBody UpdateUserTravelRequest request
    ) {
        userTravelFacade.updateUserTravel(uuid, id, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Override
    @PutMapping("/{id}/itinerary")
    public ResponseEntity<SuccessResponse> replaceUserTravelItinerary(
        @CurrentUuid String uuid,
        @PathVariable("id") final Long id,
        @Valid @RequestBody ReplaceUserTravelItineraryRequest request
    ) {
        userTravelFacade.replaceUserTravelItinerary(uuid, id, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Override
    @PostMapping("/{id}/itinerary")
    public ResponseEntity<SuccessResponse<UserTravelItineraryResponse.ItineraryPlaceResponse>> createUserTravelPlace(
        @CurrentUuid String uuid,
        @PathVariable("id") final Long id,
        @Valid @RequestBody CreateUserTravelPlaceRequest request
    ) {
        UserTravelItineraryResponse.ItineraryPlaceResponse response =
            userTravelFacade.createUserTravelPlace(uuid, id, request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Override
    @PatchMapping("/{id}/start-time/bulk")
    public ResponseEntity<?> bulkUpdateUserTravelPlaceStartTimes(
        @CurrentUuid String uuid,
        @PathVariable("id") final Long id,
        @Valid @RequestBody UpdateUserTravelPlaceStartTimesRequest request
    ) {
        userTravelFacade.bulkUpdateUserTravelPlaceStartTimes(uuid, id, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Override
    @PatchMapping("/{id}/itinerary/{userTravelPlaceId}")
    public ResponseEntity<SuccessResponse> updateUserTravelPlace(
        @CurrentUuid String uuid,
        @PathVariable("id") final Long id,
        @PathVariable("userTravelPlaceId") final Long userTravelPlaceId,
        @Valid @RequestBody UpdateUserTravelPlaceRequest request
    ) {
        userTravelFacade.updateUserTravelPlace(uuid, id, userTravelPlaceId, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Override
    @GetMapping("/upcoming")
    public ResponseEntity<SuccessResponse<UpcomingUserTravelResponse>> getUpcomingUserTravel(
        @CurrentUuid String uuid
    ) {
        UpcomingUserTravelResponse response = userTravelFacade.getUpcomingUserTravel(uuid);
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Override
    @GetMapping("/upcoming/list")
    public ResponseEntity<SuccessResponse<SliceResponse<UpcomingUserTravelListResponse>>> getUpcomingUserTravels(
        @CurrentUuid String uuid,
        @RequestParam(value = "page", defaultValue = "0") final int page,
        @RequestParam(value = "size", defaultValue = "20") final int size
    ) {
        SliceResponse<UpcomingUserTravelListResponse> response =
            userTravelFacade.getUpcomingUserTravels(uuid, page, size);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Override
    @GetMapping("/{id}/content-card")
    public ResponseEntity<SuccessResponse<UserTravelContentCardResponse>> readUserTravelContentCard(
        @CurrentUuid String uuid,
        @PathVariable("id") final Long id
    ) {
        UserTravelContentCardResponse response = userTravelFacade.readUserTravelContentCard(uuid, id);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Override
    @GetMapping("/{id}/itinerary")
    public ResponseEntity<SuccessResponse<UserTravelItineraryResponse>> readUserTravelItinerary(
        @CurrentUuid String uuid,
        @PathVariable("id") final Long id,
        @RequestParam(value = "day") final int day
    ) {
        UserTravelItineraryResponse response = userTravelFacade.readUserTravelItinerary(uuid, id, day);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}
