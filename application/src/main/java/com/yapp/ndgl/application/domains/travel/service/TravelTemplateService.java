package com.yapp.ndgl.application.domains.travel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.place.mapper.GooglePlaceTypeMapper;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplatePopularResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateSearchResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateRecommendationResponse;
import com.yapp.ndgl.application.domains.travel.event.TravelTemplateViewCountEvent;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceDetailClient;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceTextSearchRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceTextSearchResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.common.type.PlaceCategory;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.service.TravelTemplatePlaceDomainService;
import com.yapp.ndgl.domain.travel.service.UserTravelDomainService;
import com.yapp.ndgl.domain.user.service.UserDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelTemplateService {

    private final TravelTemplatePlaceDomainService travelTemplatePlaceDomainService;
    private final TravelTemplateDomainService travelTemplateDomainService;
    private final PlaceDomainService placeDomainService;
    private final GoogleMapsPlaceDetailClient googleMapsPlaceDetailClient;
    private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final UserDomainService userDomainService;
    private final UserTravelDomainService userTravelDomainService;

    @Transactional
    public void saveTravelTemplate(final SaveTravelTemplateRequest request) {
        // 1. TravelTemplate 도메인 객체 생성 및 저장 (TravelProgram은 domain-service에서 조회/생성)
        int days = request.itinerary().size();
        int nights = Math.max(days - 1, 0);
        Integer budgetPerPerson = request.budgetPerPerson() != null ? Integer.parseInt(request.budgetPerPerson()) : null;

        TravelTemplate travelTemplate = TravelTemplate.builder()
            .travelId(request.travelId())
            .traveler(request.traveler())
            .country(request.country())
            .city(request.city())
            .summary(request.summary())
            .title(request.summary())
            .budgetPerPerson(budgetPerPerson)
            .nights(nights)
            .days(days)
            .build();

        TravelTemplate savedTemplate = travelTemplateDomainService.saveWithTraveler(travelTemplate, request.traveler());
        log.info("여행 템플릿 저장 완료. templateId = {}", savedTemplate.getId());

        // 2. 각 일정의 활동을 TravelTemplatePlace 도메인 객체로 변환 및 저장
        List<TravelTemplatePlace> templatePlaces = new ArrayList<>();

        for (SaveTravelTemplateRequest.ItineraryRequest itinerary : request.itinerary()) {
            for (SaveTravelTemplateRequest.ActivityRequest activity : itinerary.activities()) {
                // 2-1. 메인 Place 조회 또는 생성 (장소명으로 텍스트 서치 후 placeId 획득)
                String googlePlaceId = searchGooglePlaceId(activity.placeName());
                Place place = findOrCreatePlace(googlePlaceId);

                // 2-2. transportationJson 직렬화
                String transportationJson = serializeToJson(activity.transportation());

                // 2-3. youtubeTipsJson 직렬화
                String youtubeTipsJson = serializeToJson(activity.youtubeTips());

                // 2-4. planB 처리
                String planBJson = processPlanB(activity.planB());

                // 2-5. TravelTemplatePlace 도메인 객체 생성
                TravelTemplatePlace templatePlace = TravelTemplatePlace.builder()
                    .travelTemplateId(savedTemplate.getId())
                    .placeId(place.getId())
                    .sequence(activity.sequence())
                    .day(itinerary.day())
                    .distanceKm(activity.distanceKm())
                    .transportationJson(transportationJson)
                    .youtubeTipsJson(youtubeTipsJson)
                    .planBJson(planBJson)
                    .estimatedDuration(activity.estimatedTime())
                    .build();

                templatePlaces.add(templatePlace);
            }
        }

        travelTemplatePlaceDomainService.saveAllFromDomain(templatePlaces);
        log.info("여행 템플릿 장소 저장 완료. templateId = {}, 장소 수 = {}", savedTemplate.getId(), templatePlaces.size());
    }

    private Place findOrCreatePlace(final String googlePlaceId) {
        // 1. DB에서 조회
        Optional<Place> existingPlace = placeDomainService.findByGooglePlaceId(googlePlaceId);
        if (existingPlace.isPresent()) {
            return existingPlace.get();
        }

        // 2. 없으면 Google Maps API로 조회
        log.info("Google Maps API에서 장소 정보를 조회합니다. googlePlaceId = {}", googlePlaceId);
        PlaceDetailsRequest request = PlaceDetailsRequest.of(googlePlaceId, "ko");
        GooglePlaceDetailsResponse response = googleMapsPlaceDetailClient.readPlaceDetails(request);

        // 3. photo[0]으로 썸네일 조회
        String thumbnail = null;
        if (response.photos() != null && !response.photos().isEmpty()) {
            GooglePlaceDetailsResponse.PhotoMeta photoMeta = response.photos().get(0);
            PlacePhotoRequest photoRequest = PlacePhotoRequest.of(
                photoMeta.name(), photoMeta.heightPx(), photoMeta.widthPx());
            thumbnail = googleMapsPlacePhotoClient.getPhotoUri(photoRequest).uri();
        }

        // 4. Place 저장
        Place newPlace = toPlace(response, thumbnail);
        return placeDomainService.save(newPlace);
    }

    private String processPlanB(final List<SaveTravelTemplateRequest.PlanBRequest> planBList) {
        if (planBList == null || planBList.isEmpty()) {
            return null;
        }

        List<PlanBInfo> planBInfos = new ArrayList<>();
        for (SaveTravelTemplateRequest.PlanBRequest planB : planBList) {
            String googlePlaceId = searchGooglePlaceId(planB.name());
            Place place = findOrCreatePlace(googlePlaceId);
            planBInfos.add(new PlanBInfo(place.getId(), planB.name(), planB.feature()));
        }

        return serializeToJson(planBInfos);
    }

    private String searchGooglePlaceId(final String placeName) {
        log.info("Google Maps Text Search API로 장소를 검색합니다. placeName = {}", placeName);
        GooglePlaceTextSearchResponse response = googleMapsPlaceDetailClient
            .searchPlacesByText(PlaceTextSearchRequest.of(placeName));

        if (response.places() == null || response.places().isEmpty()) {
            log.error("텍스트 검색 결과가 없습니다. placeName = {}", placeName);
            throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
        }

        return response.places().get(0).id();
    }

    private String serializeToJson(final Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 실패", e);
            throw new GlobalException(GoogleMapsErrorCode.RESPONSE_PARSE_FAILED);
        }
    }

    private Place toPlace(final GooglePlaceDetailsResponse response, final String thumbnail) {
        try {
            String name = null;
            if (response.name() != null) {
                name = response.name().text();
            }

            String regularOpeningHours = null;
            if (response.regularOpeningHours() != null) {
                regularOpeningHours = objectMapper.writeValueAsString(
                    response.regularOpeningHours().regularOpeningHours()
                );
            }

            String photosJson = null;
            if (response.photos() != null && !response.photos().isEmpty()) {
                photosJson = objectMapper.writeValueAsString(response.photos());
            }

            Double latitude = response.location() != null ? response.location().latitude() : null;
            Double longitude = response.location() != null ? response.location().longitude() : null;

            String priceCurrencyCode = null;
            String priceStartUnits = null;
            String priceEndUnits = null;
            if (response.priceRange() != null) {
                if (response.priceRange().startPrice() != null) {
                    priceCurrencyCode = response.priceRange().startPrice().currencyCode();
                    priceStartUnits = response.priceRange().startPrice().units();
                }
                if (response.priceRange().endPrice() != null) {
                    priceEndUnits = response.priceRange().endPrice().units();
                }
            }

            PlaceCategory category = GooglePlaceTypeMapper.toCategory(response.primaryType(), response.types());

            return Place.create(
                response.id(),
                response.formattedAddress(),
                latitude,
                longitude,
                response.rating(),
                response.nationalPhoneNumber(),
                response.internationalPhoneNumber(),
                response.websiteUri(),
                response.googleMapsUri(),
                response.userRatingCount(),
                name,
                thumbnail,
                regularOpeningHours,
                photosJson,
                priceCurrencyCode,
                priceStartUnits,
                priceEndUnits,
                category
            );
        } catch (Exception e) {
            log.error("Place 변환 실패: googlePlaceId={}", response.id(), e);
            throw new GlobalException(GoogleMapsErrorCode.RESPONSE_PARSE_FAILED);
        }
    }

    private record PlanBInfo(Long placeId, String name, String feature) {
    }

    @Transactional(readOnly = true)
    public TravelTemplateHighlightsResponse readTravelTemplateHighlights(final Long id) {

        // 템플릿 상단에 보여줄 핵심 요약 묶음
        TravelTemplate travelTemplate = travelTemplateDomainService.findById(id);

        // 조회수 증가 이벤트 발행
        eventPublisher.publishEvent(new TravelTemplateViewCountEvent(id));

        return TravelTemplateHighlightsResponse.toResponse(travelTemplate);
    }

    @Transactional(readOnly = true)
    public TravelTemplateItineraryResponse readTravelTemplateItinerary(final Long travelTemplateId, final Integer day) {
        // 여행 템플릿 장소 목록 조회 (day 파라미터에 따라 DB에서 필터링)
        List<TravelTemplatePlace> travelTemplatePlaces = travelTemplatePlaceDomainService.findPlacesByTravelTemplateIdAndDay(travelTemplateId, day);

        // placeId 목록 추출
        List<Long> placeIds = travelTemplatePlaces.stream()
            .map(TravelTemplatePlace::getPlaceId)
            .collect(Collectors.toList());

        // 장소 목록 조회
        List<Place> placeList = placeDomainService.findByIds(placeIds);
        Map<Long, Place> placeMap = placeList.stream()
            .collect(Collectors.toMap(Place::getId, place -> place));

        return TravelTemplateItineraryResponse.of(travelTemplatePlaces, placeMap, objectMapper);
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplatePopularResponse> readPopularTravelTemplates(
        final Long travelProgramId,
        final int page,
        final int size
    ) {
        SliceResponse<TravelTemplate> templates =
            travelTemplateDomainService.findPopularTemplates(travelProgramId, page, size);
        List<TravelTemplatePopularResponse> content = templates.getContent().stream()
            .map(TravelTemplatePopularResponse::from)
            .toList();
        return SliceResponse.of(content, templates.isHasNext());
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplateRecommendationResponse> readRecommendedTravelTemplates(
        final String uuid, final int page, final int size
    ) {
        Long userId = userDomainService.findByUuid(uuid).getId();
        UserTravel latestUserTravel = userTravelDomainService.findLatestUpcomingByUserId(userId).orElse(null);
        String country = latestUserTravel == null ? null : latestUserTravel.getCountry();

        SliceResponse<TravelTemplate> templates = travelTemplateDomainService.findRecommendedTemplates(country, page, size);
        List<TravelTemplateRecommendationResponse> content = templates.getContent().stream()
            .map(TravelTemplateRecommendationResponse::from)
            .toList();
        return SliceResponse.of(content, templates.isHasNext());
    }

    @Transactional(readOnly = true)
    public SliceResponse<TravelTemplateSearchResponse> searchTravelTemplates(
        final String keyword, final int page, final int size
    ) {
        SliceResponse<TravelTemplate> templates = travelTemplateDomainService.findByKeyword(keyword, page, size);
        List<TravelTemplateSearchResponse> content = templates.getContent().stream()
            .map(TravelTemplateSearchResponse::from)
            .toList();
        return SliceResponse.of(content, templates.isHasNext());
    }

}
