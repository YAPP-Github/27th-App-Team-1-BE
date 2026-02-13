package com.yapp.ndgl.application.domains.travel.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelContentCardResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelItineraryResponse;
import com.yapp.ndgl.application.domains.travel.service.UserTravelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Facade
@RequiredArgsConstructor
public class UserTravelFacade {

    private final UserTravelService userTravelService;

    public Long createUserTravel(final String uuid, final CreateUserTravelRequest request) {
        log.info("템플릿으로 사용자 여행을 생성합니다. uuid = {}, templateId = {}", uuid, request.templateId());
        return userTravelService.createUserTravel(uuid, request);
    }

    public UpcomingUserTravelResponse getUpcomingUserTravel(final String uuid) {
        log.info("다가오는 사용자 여행을 조회합니다. uuid = {}", uuid);
        return userTravelService.getUpcomingUserTravel(uuid);
    }

    public UserTravelContentCardResponse readUserTravelContentCard(final String uuid, final Long id) {
        log.info("사용자 여행 상단 정보를 조회합니다. uuid = {}, userTravelId = {}", uuid, id);
        return userTravelService.readUserTravelContentCard(uuid, id);
    }

    public UserTravelItineraryResponse readUserTravelItinerary(final String uuid, final Long id, final int day) {
        log.info("사용자 여행 일정을 조회합니다. uuid = {}, userTravelId = {}, day = {}", uuid, id, day);
        return userTravelService.readUserTravelItinerary(uuid, id, day);
    }
}
