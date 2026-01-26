package com.yapp.ndgl.domain.travel.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.entity.UserTravelEntity;
import com.yapp.ndgl.domain.travel.mapper.UserTravelMapper;
import com.yapp.ndgl.domain.travel.repository.UserTravelRepository;
import com.yapp.ndgl.domain.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTravelDomainService {

	private final UserTravelRepository userTravelRepository;

	@Transactional
	public UserTravel createUserTravel(
        final User user,
        final TravelTemplate template,
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
		return UserTravelMapper.toDomain(savedUserTravelEntity);
	}
}
