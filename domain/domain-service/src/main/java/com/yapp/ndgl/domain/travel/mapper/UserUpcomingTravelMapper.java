package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.UserUpcomingTravel;
import com.yapp.ndgl.domain.travel.query.UserUpcomingTravelSummary;

public class UserUpcomingTravelMapper {

	public static UserUpcomingTravel toDomain(final UserUpcomingTravelSummary summary) {
		if (summary == null) {
			return null;
		}

		return UserUpcomingTravel.builder()
			.id(summary.getId())
			.title(summary.getTitle())
			.country(summary.getCountry())
			.city(summary.getCity())
			.startDate(summary.getStartDate())
			.endDate(summary.getEndDate())
			.nights(summary.getNights())
			.days(summary.getDays())
			.templateId(summary.getTemplateId())
			.thumbnail(summary.getThumbnail())
			.profileImage(summary.getProfileImage())
			.build();
	}
}
