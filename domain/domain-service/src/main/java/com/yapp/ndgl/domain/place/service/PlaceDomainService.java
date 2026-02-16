package com.yapp.ndgl.domain.place.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.PlaceErrorCode;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.entity.PlaceEntity;
import com.yapp.ndgl.domain.place.mapper.PlaceMapper;
import com.yapp.ndgl.domain.place.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceDomainService {

	private final PlaceRepository placeRepository;

	/**
	 * googlePlaceId로 장소 조회 (없으면 Optional.empty 반환)
	 */
	public Optional<Place> findByGooglePlaceId(final String googlePlaceId) {
		return placeRepository.findByGooglePlaceId(googlePlaceId)
			.map(PlaceMapper::toDomain);
	}

	/**
	 * googlePlaceId로 장소 조회 (없으면 예외 발생)
	 */
	public Place readPlaceDetailByGooglePLaceId(final String googlePlaceId) {
		return placeRepository.findByGooglePlaceId(googlePlaceId)
			.map(PlaceMapper::toDomain)
			.orElseThrow(() -> {
				log.warn("[PlaceDomainService] googlePlaceId로 장소를 찾을 수 없음. googlePlaceId={}", googlePlaceId);
				return new GlobalException(PlaceErrorCode.NOT_FOUND_PLACE);
			});
	}

	public List<Place> findByIds(final List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return List.of();
		}
		return placeRepository.findAllById(ids).stream()
			.map(PlaceMapper::toDomain)
			.toList();
	}

	public Place findById(final Long id) {
		if (id == null) {
			return null;
		}
		return placeRepository.findById(id)
			.map(PlaceMapper::toDomain)
			.orElse(null);
	}

	/**
	 * googlePlaceId가 이미 존재하면 예외 발생
	 */
	public void validateNotExistsByGooglePlaceId(final String googlePlaceId) {
		if (googlePlaceId != null && placeRepository.existsByGooglePlaceId(googlePlaceId)) {
			log.warn("[PlaceDomainService] 이미 존재하는 장소. googlePlaceId={}", googlePlaceId);
			throw new GlobalException(PlaceErrorCode.ALREADY_EXISTS_PLACE);
		}
	}

	@Transactional
	public Place save(final Place place) {
		String googlePlaceId = place.getGooglePlaceId();
		validateNotExistsByGooglePlaceId(googlePlaceId);

		PlaceEntity savedEntity = placeRepository.save(PlaceMapper.toEntity(place));
		return PlaceMapper.toDomain(savedEntity);
	}
}
