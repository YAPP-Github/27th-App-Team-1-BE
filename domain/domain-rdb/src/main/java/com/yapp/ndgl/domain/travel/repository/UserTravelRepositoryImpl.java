package com.yapp.ndgl.domain.travel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.ndgl.domain.travel.entity.QTravelTemplateEntity;
import com.yapp.ndgl.domain.travel.entity.QUserTravelEntity;
import com.yapp.ndgl.domain.travel.query.QUserUpcomingTravelSummary;
import com.yapp.ndgl.domain.travel.query.UserUpcomingTravelSummary;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTravelRepositoryImpl implements UserTravelRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<UserUpcomingTravelSummary> findUpcomingUserTravelsByUserId(
		final Long userId, final LocalDate today, final Pageable pageable
	) {
		QUserTravelEntity userTravel = QUserTravelEntity.userTravelEntity;
		QTravelTemplateEntity travelTemplate = QTravelTemplateEntity.travelTemplateEntity;

		return queryFactory
			.select(new QUserUpcomingTravelSummary(
				userTravel.id,
				userTravel.title,
				userTravel.country,
				userTravel.countryName,
				userTravel.city,
				userTravel.startDate,
				userTravel.endDate,
				userTravel.nights,
				userTravel.days,
				travelTemplate.id,
				travelTemplate.thumbnail,
				travelTemplate.profileImage
			))
			.from(userTravel)
			.leftJoin(travelTemplate).on(travelTemplate.id.eq(userTravel.templateId))
			.where(
				userTravel.userId.eq(userId),
				userTravel.startDate.goe(today)
			)
			.orderBy(userTravel.startDate.asc(), userTravel.id.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}
}
