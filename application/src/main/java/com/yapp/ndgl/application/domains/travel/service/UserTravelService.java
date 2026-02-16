package com.yapp.ndgl.application.domains.travel.service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.ReplaceUserTravelItineraryRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelListResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpdateUserTravelPlaceStartTimesRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpdateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelContentCardResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelItineraryResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.PlaceErrorCode;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.UserTravelPlace;
import com.yapp.ndgl.domain.travel.UserUpcomingTravel;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.service.UserTravelDomainService;
import com.yapp.ndgl.domain.user.User;
import com.yapp.ndgl.domain.user.service.UserDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
			days,
			template.getThumbnail()
		).getId();
	}

	@Transactional
	public void updateUserTravel(final String uuid, final Long userTravelId, final UpdateUserTravelRequest request) {
		log.info("내 여행 정보를 수정합니다. uuid = {}, userTravelId = {}", uuid, userTravelId);
		User user = userDomainService.findByUuid(uuid);
		String title = request.title().trim();

		if (request.endDate().isBefore(request.startDate())) {
			log.warn(
				"내 여행 정보 수정 실패 - 날짜 순서 오류. uuid = {}, userTravelId = {}, startDate = {}, endDate = {}",
				uuid,
				userTravelId,
				request.startDate(),
				request.endDate()
			);
			throw new GlobalException(TravelErrorCode.INVALID_DATE_ORDER);
		}

		long daysBetween = ChronoUnit.DAYS.between(request.startDate(), request.endDate());
		int nights = (int)daysBetween;
		int days = nights + 1;

		userTravelDomainService.updateUserTravel(
			userTravelId,
			user.getId(),
			title,
			request.startDate(),
			request.endDate(),
			nights,
			days
		);
		log.info("내 여행 정보를 수정했습니다. uuid = {}, userTravelId = {}", uuid, userTravelId);
	}

	@Transactional
	public void replaceUserTravelItinerary(
		final String uuid,
		final Long userTravelId,
		final ReplaceUserTravelItineraryRequest request
	) {
		log.info("내 여행 일정을 전체 교체합니다. uuid = {}, userTravelId = {}, itineraryCount = {}",
			uuid, userTravelId, request.itineraries().size());
		User user = userDomainService.findByUuid(uuid);
		UserTravel userTravel = userTravelDomainService.findByIdAndUserId(userTravelId, user.getId());

		validateReplaceUserTravelItineraryRequest(userTravel, request.itineraries());

		List<UserTravelPlace> userTravelPlaces = request.itineraries().stream()
			.map(itinerary -> UserTravelPlace.create(
				userTravelId,
				itinerary.placeId(),
				itinerary.day(),
				itinerary.sequence(),
				itinerary.travelerTip(),
				itinerary.startTime(),
				itinerary.estimatedDuration()
			)).toList();

		userTravelDomainService.replaceUserTravelPlaces(userTravelId, userTravelPlaces);
		log.info("내 여행 일정을 전체 교체했습니다. uuid = {}, userTravelId = {}, itineraryCount = {}",
			uuid, userTravelId, userTravelPlaces.size());
	}

	@Transactional
	public void bulkUpdateUserTravelPlaceStartTimes(
		final String uuid, final Long userTravelId, final UpdateUserTravelPlaceStartTimesRequest request
	) {
		User user = userDomainService.findByUuid(uuid);
		userTravelDomainService.findByIdAndUserId(userTravelId, user.getId());

		Map<Long, LocalTime> startTimeByUserTravelPlaceId = request.updates().stream()
			.collect(Collectors.toMap(
				UpdateUserTravelPlaceStartTimesRequest.Item::id,
				UpdateUserTravelPlaceStartTimesRequest.Item::startTime,
				(existing, replacement) -> replacement,
				LinkedHashMap::new
			));

		List<Long> userTravelPlaceIds = startTimeByUserTravelPlaceId
			.keySet().stream()
			.toList();
		userTravelDomainService.bulkUpdateStartTime(userTravelId, userTravelPlaceIds, startTimeByUserTravelPlaceId);
	}

	private void validateReplaceUserTravelItineraryRequest(
		final UserTravel userTravel,
		final List<ReplaceUserTravelItineraryRequest.Item> itineraries
	) {
		Set<Long> placeIds = new HashSet<>();
		Map<Integer, Set<Integer>> sequencesByDay = new HashMap<>();

		for (ReplaceUserTravelItineraryRequest.Item itinerary : itineraries) {
			if (itinerary.day() > userTravel.getDays()) {
				log.warn("내 여행 일정 교체 검증 실패 - 일차 범위 초과. userTravelId = {}, maxDay = {}, requestDay = {}",
					userTravel.getId(), userTravel.getDays(), itinerary.day());
				throw new GlobalException(TravelErrorCode.INVALID_ITINERARY_REQUEST);
			}
			if (!placeIds.add(itinerary.placeId())) {
				log.warn("내 여행 일정 교체 검증 실패 - 중복 placeId. userTravelId = {}, placeId = {}",
					userTravel.getId(), itinerary.placeId());
				throw new GlobalException(TravelErrorCode.INVALID_ITINERARY_REQUEST);
			}

			Set<Integer> daySequences = sequencesByDay.computeIfAbsent(itinerary.day(), day -> new HashSet<>());
			if (!daySequences.add(itinerary.sequence())) {
				log.warn("내 여행 일정 교체 검증 실패 - 동일 일차 sequence 중복. userTravelId = {}, day = {}, sequence = {}",
					userTravel.getId(), itinerary.day(), itinerary.sequence());
				throw new GlobalException(TravelErrorCode.INVALID_ITINERARY_REQUEST);
			}
		}

		int foundPlaceCount = placeDomainService.findByIds(placeIds.stream().toList()).size();
		if (foundPlaceCount != placeIds.size()) {
			log.warn("내 여행 일정 교체 검증 실패 - 존재하지 않는 placeId 포함. userTravelId = {}, requestedCount = {}, foundCount = {}",
				userTravel.getId(), placeIds.size(), foundPlaceCount);
			throw new GlobalException(PlaceErrorCode.NOT_FOUND_PLACE);
		}
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
