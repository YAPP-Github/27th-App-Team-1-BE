package com.yapp.ndgl.application.domains.place.service;

import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.place.service.UserFavoritePlaceDomainService;
import com.yapp.ndgl.domain.user.User;
import com.yapp.ndgl.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceFavoriteService {

    private final UserDomainService userDomainService;
    private final PlaceDomainService placeDomainService;
    private final UserFavoritePlaceDomainService userFavoritePlaceDomainService;

    @Transactional
    public void addFavoritePlace(final String uuid, final String googlePlaceId) {
        User user = userDomainService.findByUuid(uuid);
        Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);
        userFavoritePlaceDomainService.addFavoritePlace(user.getId(), place.getId());
    }

    @Transactional
    public void removeFavoritePlace(final String uuid, final String googlePlaceId) {
        User user = userDomainService.findByUuid(uuid);
        Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);
        userFavoritePlaceDomainService.removeFavoritePlace(user.getId(), place.getId());
    }
}
