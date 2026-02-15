package com.yapp.ndgl.domain.travel;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpcomingTravel {

    private Long id;
    private String title;
    private String country;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer nights;
    private Integer days;
    private Long templateId;
    private String thumbnail;
    private String profileImage;

}
