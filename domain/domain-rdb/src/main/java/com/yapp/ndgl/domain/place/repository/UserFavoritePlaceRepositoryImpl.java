package com.yapp.ndgl.domain.place.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.ndgl.domain.place.entity.PlaceEntity;
import com.yapp.ndgl.domain.place.entity.QPlaceEntity;
import com.yapp.ndgl.domain.place.entity.QUserFavoritePlaceEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserFavoritePlaceRepositoryImpl implements UserFavoritePlaceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceEntity> findFavoritePlacesByUserId(final Long userId, final Pageable pageable) {
        QUserFavoritePlaceEntity userFavoritePlace = QUserFavoritePlaceEntity.userFavoritePlaceEntity;
        QPlaceEntity place = QPlaceEntity.placeEntity;

        return queryFactory
            .select(place)
            .from(userFavoritePlace)
            .join(place).on(place.id.eq(userFavoritePlace.placeId))
            .where(userFavoritePlace.userId.eq(userId))
            .orderBy(userFavoritePlace.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }
}
