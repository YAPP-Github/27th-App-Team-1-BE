package com.yapp.ndgl.domain.place.service;

import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.entity.UserFavoritePlaceEntity;
import com.yapp.ndgl.domain.place.mapper.PlaceMapper;
import com.yapp.ndgl.domain.place.repository.UserFavoritePlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFavoritePlaceDomainService {

    private final UserFavoritePlaceRepository userFavoritePlaceRepository;

    @Transactional
    public void addFavoritePlace(final Long userId, final Long placeId) {
        if (userFavoritePlaceRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            log.info("이미 즐겨찾기에 존재하는 장소입니다. userId = {}, placeId = {}", userId, placeId);
            return;
        }

        userFavoritePlaceRepository.save(
            UserFavoritePlaceEntity.builder()
                .userId(userId)
                .placeId(placeId)
                .build()
        );
    }

    @Transactional
    public void removeFavoritePlace(final Long userId, final Long placeId) {
        userFavoritePlaceRepository.deleteByUserIdAndPlaceId(userId, placeId);
    }

    @Transactional(readOnly = true)
    public SliceResponse<Place> findFavoritePlacesByUserId(final Long userId, final int page, final int size) {
        Pageable pageable = PageRequest.of(page, size + 1);
        List<Place> favoritePlaces = userFavoritePlaceRepository.findFavoritePlacesByUserId(userId, pageable).stream()
            .map(PlaceMapper::toDomain)
            .toList();

        boolean hasNext = favoritePlaces.size() > size;
        List<Place> content = favoritePlaces.stream()
            .limit(size)
            .toList();

        return SliceResponse.of(content, hasNext);
    }
}
