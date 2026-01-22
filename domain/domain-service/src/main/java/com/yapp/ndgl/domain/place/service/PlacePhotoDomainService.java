package com.yapp.ndgl.domain.place.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.domain.place.PlacePhoto;
import com.yapp.ndgl.domain.place.mapper.PlacePhotoMapper;
import com.yapp.ndgl.domain.place.entity.PlacePhotoEntity;
import com.yapp.ndgl.domain.place.repository.PlacePhotoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlacePhotoDomainService {

	private final PlacePhotoRepository placePhotoRepository;

	@Transactional(readOnly = true)
	public List<PlacePhoto> findByGooglePlaceId(final String googlePlaceId) {
		return placePhotoRepository.findByGooglePlaceId(googlePlaceId).stream()
			.map(PlacePhotoMapper::toDomain)
			.toList();
	}

	public boolean existsByPhotoName(final String photoName) {
		return placePhotoRepository.existsByPhotoName(photoName);
	}

	@Transactional
	public void saveAll(final List<PlacePhoto> placePhotos) {
		List<PlacePhotoEntity> entities = placePhotos.stream()
			.map(PlacePhotoMapper::toEntity)
			.toList();

		placePhotoRepository.saveAll(entities);
	}

	@Transactional
	public void saveAllIfNotExists(final List<PlacePhoto> placePhotos) {
		List<String> photoNames = placePhotos.stream()
			.map(PlacePhoto::getPhotoName)
			.toList();

		Set<String> existingPhotoNames = placePhotoRepository.findByPhotoNameIn(photoNames).stream()
			.map(PlacePhotoEntity::getPhotoName)
			.collect(Collectors.toSet());

		List<PlacePhoto> newPhotos = placePhotos.stream()
			.filter(photo -> !existingPhotoNames.contains(photo.getPhotoName()))
			.toList();

		saveAll(newPhotos);
	}
}
