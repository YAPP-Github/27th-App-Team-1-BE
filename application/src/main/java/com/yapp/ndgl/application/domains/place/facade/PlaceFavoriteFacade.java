package com.yapp.ndgl.application.domains.place.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.place.controller.response.PlaceFavoriteListResponse;
import com.yapp.ndgl.application.domains.place.service.PlaceFavoriteService;
import com.yapp.ndgl.common.response.SliceResponse;
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

    public SliceResponse<PlaceFavoriteListResponse> readFavoritePlaces(
        final String uuid, final int page, final int size
    ) {
        log.info("즐겨찾기 장소 목록을 조회합니다. uuid = {}, page = {}, size = {}", uuid, page, size);
        return placeFavoriteService.readFavoritePlaces(uuid, page, size);
    }
}
