package com.yapp.ndgl.domain.travel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

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

    @Transactional
    public TravelProgramEntity findOrCreateEntityByName(final String name) {
        Optional<TravelProgramEntity> existing = travelProgramRepository.findByName(name);
        if (existing.isPresent()) {
            return existing.get();
        }

        try {
            log.info("새로운 여행 프로그램을 생성합니다. name = {}", name);
            TravelProgramEntity newProgram = TravelProgramEntity.builder()
                .name(name)
                .type(TravelProgramType.YOUTUBE)
                .build();
            return travelProgramRepository.saveAndFlush(newProgram);
        } catch (DataIntegrityViolationException e) {
            log.warn("여행 프로그램 동시 생성 감지, 기존 데이터를 조회합니다. name = {}", name);
            return travelProgramRepository.findByName(name)
                .orElseThrow(() -> e);
        }
    }
}
