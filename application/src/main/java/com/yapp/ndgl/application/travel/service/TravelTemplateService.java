package com.yapp.ndgl.application.travel.service;

import com.yapp.ndgl.application.travel.controller.dto.TravelTemplateResponse;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.PlaceDomainService;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelTemplateService {

    private final TravelTemplateDomainService travelTemplateDomainService;
    private final PlaceDomainService placeDomainService;

    @Transactional(readOnly = true)
    public TravelTemplateResponse getTravelTemplate(Long id) {
        // 템플릿 조회
        TravelTemplate travelTemplate = travelTemplateDomainService.findById(id);

        // 매핑 테이블 조회
        List<TravelTemplatePlace> travelTemplatePlaces = travelTemplateDomainService
            .findPlacesByTravelTemplateId(id);

        List<Long> placeIds = travelTemplatePlaces.stream()
            .map(TravelTemplatePlace::getPlaceId)
            .collect(Collectors.toList());

        // 장소 목록 조회
        List<Place> placeList = placeDomainService.findByIds(placeIds);
        Map<Long, Place> placeMap = placeList.stream()
            .collect(Collectors.toMap(Place::getId, place -> place));

        List<TravelTemplateResponse.TravelTemplatePlaceResponse> places = travelTemplatePlaces.stream()
            .map(travelTemplatePlace -> {
                Place place = placeMap.get(travelTemplatePlace.getPlaceId());

                return new TravelTemplateResponse.TravelTemplatePlaceResponse(
                    travelTemplatePlace.getSequence(),
                    travelTemplatePlace.getDay(),
                    travelTemplatePlace.getTravelerTip(),
                    place == null ? null : new TravelTemplateResponse.PlaceResponse(
                        place.getPlaceId(),
                        place.getFormattedAddress(),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getRating(),
                        place.getNationalPhoneNumber(),
                        place.getInternationalPhoneNumber(),
                        place.getWebsiteUri(),
                        place.getGoogleMapsUri(),
                        place.getUserRatingCount()
                    )
                );
            })
            .collect(Collectors.toList());

        return new TravelTemplateResponse(
            travelTemplate.getTravelId(),
            travelTemplate.getTraveler(),
            travelTemplate.getCountry(),
            travelTemplate.getCity(),
            travelTemplate.getWeatherInfo(),
            travelTemplate.getCultureInfo(),
            travelTemplate.getFoodInfo(),
            travelTemplate.getThumbnail(),
            travelTemplate.getLink(),
            travelTemplate.getBudgetPerPerson(),
            travelTemplate.getSummary(),
            travelTemplate.getTitle(),
            travelTemplate.getNights(),
            travelTemplate.getDays(),
            places
        );
    }

}
