package com.yapp.ndgl.application.domains.travel.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelResponse;
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
}
