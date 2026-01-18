package com.yapp.ndgl.domain.place;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.domain.place.entity.PlaceEntity;
import com.yapp.ndgl.domain.place.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceDomainService {

	private final PlaceRepository placeRepository;

	public Optional<Place> findByPlaceId(final String placeId) {
		return placeRepository.findByPlaceId(placeId)
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
