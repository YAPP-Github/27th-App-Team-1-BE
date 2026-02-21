package com.yapp.ndgl.domain.travel.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.UserUpcomingTravel;
import com.yapp.ndgl.domain.travel.UserTravelPlace;
import com.yapp.ndgl.domain.travel.entity.UserTravelEntity;
import com.yapp.ndgl.domain.travel.entity.UserTravelPlaceEntity;
import com.yapp.ndgl.domain.travel.mapper.UserUpcomingTravelMapper;
import com.yapp.ndgl.domain.travel.mapper.UserTravelPlaceMapper;
import com.yapp.ndgl.domain.travel.mapper.UserTravelMapper;
import com.yapp.ndgl.domain.travel.query.UserUpcomingTravelSummary;
import com.yapp.ndgl.domain.travel.repository.UserTravelPlaceRepository;
import com.yapp.ndgl.domain.travel.repository.UserTravelRepository;
import com.yapp.ndgl.domain.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTravelDomainService {

	private final UserTravelRepository userTravelRepository;
	private final UserTravelPlaceRepository userTravelPlaceRepository;

	@Transactional
	public UserTravel createUserTravelWithPlaces(
		final User user,
		final TravelTemplate template,
		final List<TravelTemplatePlace> templatePlaces,
		final LocalDate startDate,
		final LocalDate endDate,
		final int nights,
		final int days,
		final String thumbnail) {

		UserTravel userTravel = UserTravel.create(
			user.getId(),
			template.getId(),
			template.getTitle(),
			template.getCountry(),
			template.getCountryName(),
			template.getCity(),
			startDate,
			endDate,
			nights,
			days,
			thumbnail
		);

		UserTravelEntity savedUserTravelEntity = userTravelRepository.save(UserTravelMapper.toEntity(userTravel));
		UserTravel savedUserTravel = UserTravelMapper.toDomain(savedUserTravelEntity);

		List<UserTravelPlace> userTravelPlaces = templatePlaces.stream()
			.filter(templatePlace -> templatePlace.getDay() <= days)
			.map(templatePlace -> UserTravelPlace.create(
				savedUserTravel.getId(),
				templatePlace.getPlaceId(),
				templatePlace.getDay(),
				templatePlace.getSequence(),
				null, // TODO: UserTravel 정규화 시 travelerTipsJson에서 변환 필요
				null,
				templatePlace.getEstimatedDuration()
			))
			.toList();

		List<UserTravelPlaceEntity> entities = userTravelPlaces.stream()
			.map(UserTravelPlaceMapper::toEntity)
			.toList();

		userTravelPlaceRepository.saveAll(entities);

		return savedUserTravel;
	}

	@Transactional(readOnly = true)
	public Optional<UserTravel> findLatestUpcomingByUserId(final Long userId) {
		LocalDate today = LocalDate.now();
		return userTravelRepository.findTopByUserIdAndStartDateGreaterThanEqualOrderByStartDateAsc(userId, today)
			.map(UserTravelMapper::toDomain);
	}

	@Transactional(readOnly = true)
	public Optional<UserTravelPlace> findFirstPlaceByUserTravelId(final Long userTravelId) {
		return userTravelPlaceRepository.findTopByUserTravelIdOrderByDayAscSequenceAsc(userTravelId)
			.map(UserTravelPlaceMapper::toDomain);
	}

	@Transactional(readOnly = true)
	public UserTravel findByIdAndUserId(final Long userTravelId, final Long userId) {
		return userTravelRepository.findByIdAndUserId(userTravelId, userId)
			.map(UserTravelMapper::toDomain)
			.orElseThrow(() -> new GlobalException(TravelErrorCode.NOT_FOUND_USER_TRAVEL));
	}

	@Transactional
	public void updateUserTravel(
		final Long userTravelId,
		final Long userId,
		final String title,
		final LocalDate startDate,
		final LocalDate endDate,
		final int nights,
		final int days
	) {
		UserTravelEntity userTravelEntity = userTravelRepository.findByIdAndUserId(userTravelId, userId)
			.orElseThrow(() -> new GlobalException(TravelErrorCode.NOT_FOUND_USER_TRAVEL));
		userTravelEntity.updateTravelInfo(title, startDate, endDate, nights, days);
	}

	@Transactional
	public void replaceUserTravelPlaces(final Long userTravelId, final List<UserTravelPlace> userTravelPlaces) {
		userTravelPlaceRepository.deleteByUserTravelId(userTravelId);
		if (userTravelPlaces.isEmpty()) {
			return;
		}

		List<UserTravelPlaceEntity> entities = userTravelPlaces.stream()
			.map(UserTravelPlaceMapper::toEntity)
			.toList();
		userTravelPlaceRepository.saveAll(entities);
	}

	@Transactional(readOnly = true)
	public List<UserTravelPlace> findPlacesByUserTravelIdAndDay(final Long userTravelId, final int day) {
		List<UserTravelPlaceEntity> placeEntities =
			userTravelPlaceRepository.findByUserTravelIdAndDayOrderBySequenceAsc(userTravelId, day);

		return placeEntities.stream()
			.map(UserTravelPlaceMapper::toDomain)
			.toList();
	}

	@Transactional
	public void bulkUpdateStartTime(
		final Long userTravelId,
		final List<Long> userTravelPlaceIds,
		final Map<Long, LocalTime> startTimeByUserTravelPlaceId
	) {
		if (userTravelPlaceIds.isEmpty()) {
			return;
		}

		List<UserTravelPlaceEntity> placeEntities = userTravelPlaceRepository.findByIdIn(userTravelPlaceIds);
		if (placeEntities.size() != userTravelPlaceIds.size()) {
			throw new GlobalException(TravelErrorCode.NOT_FOUND_USER_TRAVEL);
		}

		boolean allBelongToUserTravel = placeEntities.stream()
			.allMatch(entity -> entity.getUserTravelId().equals(userTravelId));
		if (!allBelongToUserTravel) {
			throw new GlobalException(TravelErrorCode.NOT_FOUND_USER_TRAVEL);
		}

		for (UserTravelPlaceEntity placeEntity : placeEntities) {
			placeEntity.updateStartTime(startTimeByUserTravelPlaceId.get(placeEntity.getId()));
		}
	}

	@Transactional(readOnly = true)
	public SliceResponse<UserUpcomingTravel> findUpcomingTravelsByUserId(
		final Long userId, final int page, final int size
	) {
		LocalDate today = LocalDate.now();
		Pageable pageable = PageRequest.of(page, size + 1);

		List<UserUpcomingTravelSummary> summaries =
			userTravelRepository.findUpcomingUserTravelsByUserId(userId, today, pageable);

		boolean hasNext = summaries.size() > size;
		List<UserUpcomingTravel> content = summaries.stream()
			.limit(size)
			.map(UserUpcomingTravelMapper::toDomain)
			.toList();

		return SliceResponse.of(content, hasNext);
	}
}
