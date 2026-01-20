package com.yapp.ndgl.domain.travel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.mapper.TravelTemplateMapper;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.entity.TravelTemplatePlaceEntity;
import com.yapp.ndgl.domain.travel.repository.TravelTemplatePlaceRepository;
import com.yapp.ndgl.domain.travel.repository.TravelTemplateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelTemplateDomainService {

    private final TravelTemplateRepository travelTemplateRepository;
    private final TravelTemplatePlaceRepository travelTemplatePlaceRepository;

    public TravelTemplate findById(Long id) {
        return travelTemplateRepository.findById(id)
            .map(TravelTemplateMapper::toDomain)
            .orElseThrow(() -> new GlobalException(TravelErrorCode.NOT_FOUND_TRAVEL_TEMPLATE));
    }

    public List<TravelTemplatePlace> findPlacesByTravelTemplateId(Long travelTemplateId) {
        List<TravelTemplatePlaceEntity> placeEntities = travelTemplatePlaceRepository
            .findByTravelTemplateIdOrderByDayAscSequenceAsc(travelTemplateId);

        return placeEntities.stream()
            .map(entity -> TravelTemplatePlace.builder()
                .id(entity.getId())
                .sequence(entity.getSequence())
                .day(entity.getDay())
                .travelerTip(entity.getTravelerTip())
                .placeId(entity.getPlaceId())
                .build())
            .collect(Collectors.toList());
    }
}
