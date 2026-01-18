package com.yapp.ndgl.domain.travel.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travel_templates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelTemplateEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 255)
    private String travelId;

    @Column(nullable = false, length = 100)
    private String traveler;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(name = "weather_info", length = 1000)
    private String weatherInfo;

    @Column(name = "culture_info", length = 1000)
    private String cultureInfo;

    @Column(name = "food_info", length = 1000)
    private String foodInfo;

    @Column(name = "thumbnail", length = 1000)
    private String thumbnail;

    @Column(name = "link", length = 500)
    private String link;

    @Column(name = "budget_per_person")
    private Integer budgetPerPerson;

    @Column(name = "summary", length = 2000)
    private String summary;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "nights")
    private Integer nights;

    @Column(name = "days")
    private Integer days;

    @Builder
    public TravelTemplateEntity(
        final String travelId,
        final String traveler,
        final String country,
        final String city,
        final String weatherInfo,
        final String cultureInfo,
        final String foodInfo,
        final String thumbnail,
        final String link,
        final Integer budgetPerPerson,
        final String summary,
        final String title,
        final Integer nights,
        final Integer days
    ) {
        this.travelId = travelId;
        this.traveler = traveler;
        this.country = country;
        this.city = city;
        this.weatherInfo = weatherInfo;
        this.cultureInfo = cultureInfo;
        this.foodInfo = foodInfo;
        this.thumbnail = thumbnail;
        this.link = link;
        this.budgetPerPerson = budgetPerPerson;
        this.summary = summary;
        this.title = title;
        this.nights = nights;
        this.days = days;
    }
}
