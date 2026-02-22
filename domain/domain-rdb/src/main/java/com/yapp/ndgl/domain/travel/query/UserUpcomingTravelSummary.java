package com.yapp.ndgl.domain.travel.query;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class UserUpcomingTravelSummary {

	private final Long id;
	private final String title;
	private final String country;
	private final String countryName;
	private final String city;
	private final LocalDate startDate;
	private final LocalDate endDate;
	private final Integer nights;
	private final Integer days;

	// template
	private final Long templateId;
	private final String thumbnail;
	private final String profileImage;

	@QueryProjection
	public UserUpcomingTravelSummary(
		final Long id,
		final String title,
		final String country,
		final String countryName,
		final String city,
		final LocalDate startDate,
		final LocalDate endDate,
		final Integer nights,
		final Integer days,
		final Long templateId,
		final String thumbnail,
		final String profileImage
	) {
		this.id = id;
		this.title = title;
		this.country = country;
		this.countryName = countryName;
		this.city = city;
		this.startDate = startDate;
		this.endDate = endDate;
		this.nights = nights;
		this.days = days;
		this.templateId = templateId;
		this.thumbnail = thumbnail;
		this.profileImage = profileImage;
	}
}
