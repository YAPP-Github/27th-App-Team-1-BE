package com.yapp.ndgl.application.domains.travel.facade;

import java.util.List;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelProgramResponse;
import com.yapp.ndgl.application.domains.travel.service.TravelProgramService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Facade
@RequiredArgsConstructor
public class TravelProgramFacade {

    private final TravelProgramService travelProgramService;

    public List<TravelProgramResponse> readTravelPrograms() {
        log.info("여행 프로그램 목록을 조회합니다.");
        return travelProgramService.readTravelPrograms();
    }
}
