package com.yapp.ndgl.application.domains.travel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.place.mapper.GooglePlaceTypeMapper;
import com.yapp.ndgl.application.domains.travel.controller.dto.SaveTravelTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateHighlightsResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateItineraryResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplatePopularResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateRecommendationResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.TravelTemplateSearchResponse;
import com.yapp.ndgl.application.domains.travel.event.TravelTemplateViewCountEvent;
import com.yapp.ndgl.application.domains.travel.service.dto.YouTubeVideoInfo;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceDetailClient;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceTextSearchRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceTextSearchResponse;
import com.yapp.ndgl.clients.google.youtube.YouTubeDataClient;
import com.yapp.ndgl.clients.google.youtube.dto.response.YouTubeChannelResponse;
import com.yapp.ndgl.clients.google.youtube.dto.response.YouTubeVideoResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.common.type.PlaceCategory;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.service.TravelProgramDomainService;
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
    private final TravelProgramDomainService travelProgramDomainService;
    private final PlaceDomainService placeDomainService;
    private final GoogleMapsPlaceDetailClient googleMapsPlaceDetailClient;
    private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;
    private final YouTubeDataClient youTubeDataClient;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final UserDomainService userDomainService;
    private final UserTravelDomainService userTravelDomainService;

    /**
     * Phase 1: 외부 API 호출로 모든 장소 데이터를 수집한다. (트랜잭션 없음)
     */
    public Map<String, Place> resolveAllPlaces(final SaveTravelTemplateRequest request) {
        Map<String, Place> resolvedPlaces = new HashMap<>();

        for (SaveTravelTemplateRequest.ItineraryRequest itinerary : request.itinerary()) {
            for (SaveTravelTemplateRequest.ActivityRequest activity : itinerary.activities()) {
                resolvedPlaces.computeIfAbsent(activity.placeName(), this::resolvePlace);

                if (activity.planB() != null) {
                    for (SaveTravelTemplateRequest.PlanBRequest planB : activity.planB()) {
                        resolvedPlaces.computeIfAbsent(planB.name(), this::resolvePlace);
                    }
                }
            }
        }

        return resolvedPlaces;
    }

    /**
     * Phase 1: YouTube Data API를 호출하여 영상 및 채널 정보를 수집한다. (트랜잭션 없음)
     */
    public YouTubeVideoInfo resolveYouTubeInfo(final String link) {
        log.info("YouTube Data API를 호출하여 영상 정보를 수집합니다. link = {}", link);

        String videoId = youTubeDataClient.extractVideoId(link);

        YouTubeVideoResponse videoResponse = youTubeDataClient.readVideoInfo(videoId);
        YouTubeVideoResponse.Item videoItem = videoResponse.items().get(0);

        String title = videoItem.snippet().title();
        String thumbnail = videoItem.snippet().thumbnails().bestThumbnailUrl();
        String channelId = videoItem.snippet().channelId();

        YouTubeChannelResponse channelResponse = youTubeDataClient.readChannelInfo(channelId);
        YouTubeChannelResponse.Item channelItem = channelResponse.items().get(0);

        String channelName = channelItem.snippet().title();
        String channelProfileImage = channelItem.snippet().thumbnails().bestThumbnailUrl();

        return new YouTubeVideoInfo(title, thumbnail, channelName, channelProfileImage);
    }

    private Place resolvePlace(final String placeName) {
        String googlePlaceId = searchGooglePlaceId(placeName);
        if (googlePlaceId == null) {
            return null;
        }

        Optional<Place> existingPlace = placeDomainService.findByGooglePlaceId(googlePlaceId);
        if (existingPlace.isPresent()) {
            return existingPlace.get();
        }

        log.info("Google Maps API에서 장소 정보를 조회합니다. googlePlaceId = {}", googlePlaceId);
        PlaceDetailsRequest request = PlaceDetailsRequest.of(googlePlaceId, "ko");
        GooglePlaceDetailsResponse response = googleMapsPlaceDetailClient.readPlaceDetails(request);

        String thumbnail = null;
        if (response.photos() != null && !response.photos().isEmpty()) {
            GooglePlaceDetailsResponse.PhotoMeta photoMeta = response.photos().get(0);
            PlacePhotoRequest photoRequest = PlacePhotoRequest.of(
                photoMeta.name(), photoMeta.heightPx(), photoMeta.widthPx());
            thumbnail = googleMapsPlacePhotoClient.getPhotoUri(photoRequest).uri();
        }

        return toPlace(response, thumbnail);
    }

    private String searchGooglePlaceId(final String placeName) {
        log.info("Google Maps Text Search API로 장소를 검색합니다. placeName = {}", placeName);
        GooglePlaceTextSearchResponse response = googleMapsPlaceDetailClient
            .searchPlacesByText(PlaceTextSearchRequest.of(placeName));

        if (response.places() == null || response.places().isEmpty()) {
            log.warn("텍스트 검색 결과가 없습니다. 해당 장소를 건너뜁니다. placeName = {}", placeName);
            return null;
        }

        return response.places().get(0).id();
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
