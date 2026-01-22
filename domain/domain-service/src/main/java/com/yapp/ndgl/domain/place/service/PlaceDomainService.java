package com.yapp.ndgl.domain.place.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.mapper.PlaceMapper;
import com.yapp.ndgl.domain.place.entity.PlaceEntity;
import com.yapp.ndgl.domain.place.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceDomainService {

	private final PlaceRepository placeRepository;

	public Optional<Place> findByGooglePlaceId(final String googlePlaceId) {
		return placeRepository.findByGooglePlaceId(googlePlaceId)
			.map(PlaceMapper::toDomain);
	}

	public List<Place> findByIds(final List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return List.of();
		}
		return placeRepository.findAllById(ids).stream()
			.map(PlaceMapper::toDomain)
			.toList();
	}

	@Transactional
	public Place save(final Place place) {
		PlaceEntity savedEntity = placeRepository.save(PlaceMapper.toEntity(place));
		return PlaceMapper.toDomain(savedEntity);
	}
}
