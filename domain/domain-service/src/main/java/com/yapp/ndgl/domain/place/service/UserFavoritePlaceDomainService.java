package com.yapp.ndgl.domain.place.service;

import com.yapp.ndgl.domain.place.entity.UserFavoritePlaceEntity;
import com.yapp.ndgl.domain.place.repository.UserFavoritePlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
