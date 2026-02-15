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
import com.yapp.ndgl.domain.travel.TravelProgram;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.entity.TravelProgramEntity;
import com.yapp.ndgl.domain.travel.entity.TravelTemplateEntity;
import com.yapp.ndgl.domain.travel.entity.TravelTemplatePlaceEntity;
import com.yapp.ndgl.domain.travel.mapper.TravelTemplateMapper;
import com.yapp.ndgl.domain.travel.repository.TravelProgramRepository;
import com.yapp.ndgl.domain.travel.repository.TravelTemplatePlaceRepository;
import com.yapp.ndgl.domain.travel.repository.TravelTemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelTemplateDomainService {

    private final TravelTemplateRepository travelTemplateRepository;
    private final TravelTemplatePlaceRepository travelTemplatePlaceRepository;
    private final TravelProgramRepository travelProgramRepository;

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
            .map(entity -> TravelTemplatePlace.createWithId(
                entity.getId(),
                entity.getTravelTemplateId(),
                entity.getPlaceId(),
                entity.getSequence(),
                entity.getDay(),
                entity.getDistanceKm(),
                entity.getTransportationJson(),
                entity.getTravelerTipsJson(),
                entity.getPlanBJson(),
                entity.getEstimatedDuration()))
            .toList();
    }

    @Transactional
    public void incrementViewCount(final Long id) {
        travelTemplateRepository.incrementViewCount(id);
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplate> findPopularTemplates(
        final Long travelProgramId, final int page, final int size
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
        final String country, final int page, final int size
    ) {
        Pageable pageable = PageRequest.of(page, size + 1);

        List<TravelTemplateEntity> entities = travelTemplateRepository.findRandomTemplates(country, pageable);

        boolean hasNext = entities.size() > size;
        List<TravelTemplate> content = entities.stream()
            .limit(size)
            .map(TravelTemplateMapper::toDomain)
            .toList();

        return SliceResponse.of(content, hasNext);
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplate> findByKeyword(
        final String keyword, final int page, final int size
    ) {
        String normalizedKeyword = normalizeKeyword(keyword);
        if (normalizedKeyword == null) {
            return SliceResponse.of(List.of(), false);
        }

        Pageable pageable = PageRequest.of(page, size + 1);
        List<TravelTemplateEntity> entities =
            travelTemplateRepository.findByKeyword(normalizedKeyword, pageable);

        boolean hasNext = entities.size() > size;
        List<TravelTemplate> content = entities.stream()
            .limit(size)
            .map(TravelTemplateMapper::toDomain)
            .toList();

        return SliceResponse.of(content, hasNext);
    }

    /**
     * 여행 템플릿과 여행 프로그램을 함께 생성한다.
     */
    @Transactional
    public TravelTemplate createTravelTemplate(final TravelTemplate travelTemplate, final TravelProgram travelProgram) {
        TravelProgramEntity travelProgramEntity = travelProgramRepository.getReferenceById(travelProgram.getId());
        TravelTemplateEntity travelTemplateEntity = TravelTemplateMapper.toEntity(travelTemplate, travelProgramEntity);
        TravelTemplateEntity savedTravelTemplate = travelTemplateRepository.save(travelTemplateEntity);
        return TravelTemplateMapper.toDomain(savedTravelTemplate);
    }

    private String normalizeKeyword(final String keyword) {
        if (keyword == null) {
            return null;
        }
        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
