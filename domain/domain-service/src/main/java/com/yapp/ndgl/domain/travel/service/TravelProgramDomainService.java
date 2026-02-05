package com.yapp.ndgl.domain.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.domain.travel.TravelProgram;
import com.yapp.ndgl.domain.travel.mapper.TravelProgramMapper;
import com.yapp.ndgl.domain.travel.repository.TravelProgramRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelProgramDomainService {

    private final TravelProgramRepository travelProgramRepository;

    @Transactional(readOnly = true)
    public List<TravelProgram> findAll() {
        return travelProgramRepository.findAll().stream()
            .map(TravelProgramMapper::toDomain)
            .toList();
    }
}
