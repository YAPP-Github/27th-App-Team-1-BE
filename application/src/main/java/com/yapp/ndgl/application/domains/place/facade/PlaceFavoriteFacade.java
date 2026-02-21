package com.yapp.ndgl.application.domains.place.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.place.service.PlaceFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Facade
@RequiredArgsConstructor
public class PlaceFavoriteFacade {

    private final PlaceFavoriteService placeFavoriteService;

    public void addFavoritePlace(final String uuid, final String googlePlaceId) {
        log.info("장소 즐겨찾기를 추가합니다. uuid = {}, googlePlaceId = {}", uuid, googlePlaceId);
        placeFavoriteService.addFavoritePlace(uuid, googlePlaceId);
    }

    public void removeFavoritePlace(final String uuid, final String googlePlaceId) {
        log.info("장소 즐겨찾기를 삭제합니다. uuid = {}, googlePlaceId = {}", uuid, googlePlaceId);
        placeFavoriteService.removeFavoritePlace(uuid, googlePlaceId);
    }
}
