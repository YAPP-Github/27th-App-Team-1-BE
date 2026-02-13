package com.yapp.ndgl.domain.travel.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.UserTravelPlace;
import com.yapp.ndgl.domain.travel.entity.UserTravelEntity;
import com.yapp.ndgl.domain.travel.entity.UserTravelPlaceEntity;
import com.yapp.ndgl.domain.travel.mapper.UserTravelPlaceMapper;
import com.yapp.ndgl.domain.travel.mapper.UserTravelMapper;
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
		final int days) {

		UserTravel userTravel = UserTravel.create(
			user.getId(),
			template.getId(),
			template.getTitle(),
			template.getCountry(),
			template.getCity(),
			startDate,
			endDate,
			nights,
			days
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
				null, // TODO: UserTravel 정규화 시 youtubeTipsJson에서 변환 필요
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

	@Transactional(readOnly = true)
	public List<UserTravelPlace> findPlacesByUserTravelIdAndDay(final Long userTravelId, final int day) {
		List<UserTravelPlaceEntity> placeEntities =
			userTravelPlaceRepository.findByUserTravelIdAndDayOrderBySequenceAsc(userTravelId, day);

		return placeEntities.stream()
			.map(UserTravelPlaceMapper::toDomain)
			.toList();
	}
}
