package com.yapp.ndgl.application.domains.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.application.domains.travel.controller.dto.TravelProgramResponse;
import com.yapp.ndgl.domain.travel.TravelProgram;
import com.yapp.ndgl.domain.travel.service.TravelProgramDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelProgramService {

    private final TravelProgramDomainService travelProgramDomainService;

    @Transactional(readOnly = true)
    public List<TravelProgramResponse> readTravelPrograms() {
        List<TravelProgram> programs = travelProgramDomainService.findAll();
        return programs.stream()
            .map(TravelProgramResponse::from)
            .toList();
    }
}
