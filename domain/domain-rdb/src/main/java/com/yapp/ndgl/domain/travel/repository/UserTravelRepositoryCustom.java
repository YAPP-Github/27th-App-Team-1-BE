package com.yapp.ndgl.domain.travel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yapp.ndgl.domain.travel.query.UserUpcomingTravelSummary;

public interface UserTravelRepositoryCustom {

	List<UserUpcomingTravelSummary> findUpcomingUserTravelsByUserId(
		Long userId, LocalDate today, Pageable pageable);
}
