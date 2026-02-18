package com.yapp.ndgl.domain.travel;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTravel {

    private Long id;
    private Long userId;
    private Long templateId;
    private String title;
    private String country;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer nights;
    private Integer days;
    private String thumbnail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserTravel create(
        final Long userId,
        final Long templateId,
        final String title,
        final String country,
        final String city,
        final LocalDate startDate,
        final LocalDate endDate,
        final Integer nights,
        final Integer days,
        final String thumbnail
    ) {
        LocalDateTime now = LocalDateTime.now();

        return UserTravel.builder()
            .userId(userId)
            .templateId(templateId)
            .title(title)
            .country(country)
            .city(city)
            .startDate(startDate)
            .endDate(endDate)
            .nights(nights)
            .days(days)
            .thumbnail(thumbnail)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }
}
