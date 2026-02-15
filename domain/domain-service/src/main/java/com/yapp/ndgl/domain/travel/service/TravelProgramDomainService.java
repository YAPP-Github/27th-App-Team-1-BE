package com.yapp.ndgl.domain.travel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.domain.travel.TravelProgram;
import com.yapp.ndgl.domain.travel.entity.TravelProgramEntity;
import com.yapp.ndgl.domain.travel.mapper.TravelProgramMapper;
import com.yapp.ndgl.domain.travel.repository.TravelProgramRepository;
import com.yapp.ndgl.domain.travel.type.TravelProgramType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    @Transactional(readOnly = true)
    public Optional<TravelProgramEntity> findByName(final String name) {
        return travelProgramRepository.findByName(name);
    }

    @Transactional
    public TravelProgramEntity save(final TravelProgramEntity travelProgramEntity) {
        return travelProgramRepository.save(travelProgramEntity);
    }

    /**
     * 이름으로 TravelProgram을 조회하고, 없으면 새로 생성하여 entity를 반환한다.
     */
    @Transactional
    public TravelProgramEntity findOrCreateEntityByName(final String name) {
        return travelProgramRepository.findByName(name)
            .orElseGet(() -> {
                log.info("새로운 여행 프로그램을 생성합니다. name = {}", name);
                TravelProgramEntity newProgram = TravelProgramEntity.builder()
                    .name(name)
                    .type(TravelProgramType.YOUTUBE)
                    .build();
                return travelProgramRepository.save(newProgram);
            });
    }
}
