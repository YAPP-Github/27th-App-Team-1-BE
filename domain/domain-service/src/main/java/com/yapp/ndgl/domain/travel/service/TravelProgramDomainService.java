package com.yapp.ndgl.domain.travel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
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

    public TravelProgram createTravelProgram(final String travelerName, final String profileImage, final TravelProgramType programType) {

        try {
            TravelProgram travelProgram = TravelProgram.create(travelerName, profileImage, programType);
            TravelProgramEntity travelProgramEntity = travelProgramRepository.saveAndFlush(
                TravelProgramMapper.toEntity(travelProgram)
            );
            return TravelProgramMapper.toDomain(travelProgramEntity);
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(TravelErrorCode.ALREADY_EXISTS_TRAVEL_PROGRAM);
        }
    }

    @Transactional(readOnly = true)
    public Optional<TravelProgram> findByName(final String name) {
        return travelProgramRepository.findByName(name)
            .map(TravelProgramMapper::toDomain);
    }

    public TravelProgramEntity getReferenceById(final Long id) {
        return travelProgramRepository.getReferenceById(id);
    }
}
