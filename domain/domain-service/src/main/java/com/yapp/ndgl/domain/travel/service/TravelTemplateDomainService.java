package com.yapp.ndgl.domain.travel.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.entity.TravelTemplateEntity;
import com.yapp.ndgl.domain.travel.entity.TravelTemplatePlaceEntity;
import com.yapp.ndgl.domain.travel.mapper.TravelTemplateMapper;
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

    @Transactional(readOnly = true)
    public TravelTemplate findById(final Long id) {
        return travelTemplateRepository.findById(id)
            .map(TravelTemplateMapper::toDomain)
            .orElseThrow(() -> new GlobalException(TravelErrorCode.NOT_FOUND_TRAVEL_TEMPLATE));
    }

    @Transactional(readOnly = true)
    public List<TravelTemplatePlace> findPlacesByTravelTemplateId(final Long travelTemplateId) {
        List<TravelTemplatePlaceEntity> placeEntities = travelTemplatePlaceRepository
            .findByTravelTemplateIdOrderByDayAscSequenceAsc(travelTemplateId);

        return placeEntities.stream()
            .map(entity -> TravelTemplatePlace.builder()
                .id(entity.getId())
                .travelTemplateId(entity.getTravelTemplateId())
                .sequence(entity.getSequence())
                .day(entity.getDay())
                .distanceKm(entity.getDistanceKm())
                .transportationJson(entity.getTransportationJson())
                .youtubeTipsJson(entity.getYoutubeTipsJson())
                .planBJson(entity.getPlanBJson())
                .placeId(entity.getPlaceId())
                .estimatedDuration(entity.getEstimatedDuration())
                .build())
            .toList();
    }

    @Transactional
    public void incrementViewCount(final Long id) {
        travelTemplateRepository.incrementViewCount(id);
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplate> findPopularTemplates(
        final Long travelProgramId,
        final int page,
        final int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<TravelTemplateEntity> entities = travelProgramId == null
            ? travelTemplateRepository.findAllByOrderByViewCountDesc(pageable)
            : travelTemplateRepository.findByTravelProgramIdOrderByViewCountDesc(travelProgramId, pageable);

        Slice<TravelTemplate> templates = entities.map(TravelTemplateMapper::toDomain);
        return SliceResponse.of(templates.getContent(), templates.hasNext());
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplate> findRecommendedTemplates(
        final String country,
        final int page,
        final int size
    ) {
        int limit = size + 1;
        int offset = Math.max(page, 0) * size;

        List<TravelTemplateEntity> entities = travelTemplateRepository.findRandomTemplates(
            country,
            limit,
            offset
        );

        boolean hasNext = entities.size() > size;
        List<TravelTemplate> content = entities.stream()
            .limit(size)
            .map(TravelTemplateMapper::toDomain)
            .toList();

        return SliceResponse.of(content, hasNext);
    }

}
