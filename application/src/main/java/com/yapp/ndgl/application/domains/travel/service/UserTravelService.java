package com.yapp.ndgl.application.domains.travel.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelListResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelContentCardResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelItineraryResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.UserUpcomingTravel;
import com.yapp.ndgl.domain.travel.UserTravelPlace;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.service.UserTravelDomainService;
import com.yapp.ndgl.domain.user.User;
import com.yapp.ndgl.domain.user.service.UserDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTravelService {

	private final UserTravelDomainService userTravelDomainService;
	private final TravelTemplateDomainService travelTemplateDomainService;
	private final UserDomainService userDomainService;
	private final PlaceDomainService placeDomainService;
	private final ObjectMapper objectMapper;

	@Transactional
	public Long createUserTravel(final String uuid, final CreateUserTravelRequest request) {
		User user = userDomainService.findByUuid(uuid);

		TravelTemplate template = travelTemplateDomainService.findById(request.templateId());

		if (request.endDate().isBefore(request.startDate())) {
			throw new GlobalException(TravelErrorCode.INVALID_DATE_ORDER);
		}

		long daysBetween = ChronoUnit.DAYS.between(request.startDate(), request.endDate());
		int nights = (int)daysBetween;
		int days = nights + 1;

		List<TravelTemplatePlace> templatePlaces = travelTemplateDomainService.findPlacesByTravelTemplateId(
			template.getId());

		return userTravelDomainService.createUserTravelWithPlaces(
			user,
			template,
			templatePlaces,
			request.startDate(),
			request.endDate(),
			nights,
			days
		).getId();
	}

	@Transactional(readOnly = true)
	public UpcomingUserTravelResponse getUpcomingUserTravel(final String uuid) {
		User user = userDomainService.findByUuid(uuid);
		UserTravel upcomingTravel = userTravelDomainService.findLatestUpcomingByUserId(user.getId()).orElse(null);
		if (upcomingTravel == null) {
			return null;
		}

		// TODO 현재 시간 기준으로 조회
		UserTravelPlace upcomingPlace =
			userTravelDomainService.findFirstPlaceByUserTravelId(upcomingTravel.getId()).orElse(null);

		Place place = upcomingPlace == null ? null : placeDomainService.findById(upcomingPlace.getPlaceId());

		return UpcomingUserTravelResponse.of(upcomingTravel, upcomingPlace, place, objectMapper);
	}

	@Transactional(readOnly = true)
	public SliceResponse<UpcomingUserTravelListResponse> getUpcomingUserTravels(
		final String uuid, final int page, final int size
	) {
		User user = userDomainService.findByUuid(uuid);
		SliceResponse<UserUpcomingTravel> upcomingTravels =
			userTravelDomainService.findUpcomingTravelsByUserId(user.getId(), page, size);

		List<UpcomingUserTravelListResponse> content = upcomingTravels.getContent().stream()
			.map(UpcomingUserTravelListResponse::from)
			.toList();
		return SliceResponse.of(content, upcomingTravels.isHasNext());
	}

	@Transactional(readOnly = true)
	public UserTravelContentCardResponse readUserTravelContentCard(final String uuid, final Long userTravelId) {
		User user = userDomainService.findByUuid(uuid);
		UserTravel userTravel = userTravelDomainService.findByIdAndUserId(userTravelId, user.getId());
		return UserTravelContentCardResponse.from(userTravel);
	}

	@Transactional(readOnly = true)
	public UserTravelItineraryResponse readUserTravelItinerary(
		final String uuid, final Long userTravelId, final int day
	) {
		User user = userDomainService.findByUuid(uuid);
		UserTravel userTravel = userTravelDomainService.findByIdAndUserId(userTravelId, user.getId());

		List<UserTravelPlace> userTravelPlaces = userTravelDomainService
			.findPlacesByUserTravelIdAndDay(userTravel.getId(), day);

		List<Long> placeIds = userTravelPlaces.stream()
			.map(UserTravelPlace::getPlaceId)
			.toList();
		Map<Long, Place> placeMap = placeDomainService.findByIds(placeIds).stream()
			.collect(Collectors.toMap(Place::getId, place -> place));

		return UserTravelItineraryResponse.of(userTravelPlaces, placeMap, objectMapper);
	}
}
