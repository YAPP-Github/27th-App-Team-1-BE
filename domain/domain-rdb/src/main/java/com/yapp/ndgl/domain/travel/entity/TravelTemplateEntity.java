package com.yapp.ndgl.domain.travel.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travel_templates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelTemplateEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_program_id", nullable = false)
    private TravelProgramEntity travelProgram;

    @Column(nullable = false, length = 100)
    private String traveler;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 50)
    private String continent;

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

    @Column(nullable = false, name = "view_count")
    @ColumnDefault("0")
    private long viewCount = 0L;

    @Column(name = "budget_per_person")
    private Integer budgetPerPerson;

    @Column(nullable = false, name = "summary", length = 2000)
    private String summary;

    @Column(nullable = false, name = "title", length = 500)
    private String title;

    @Column(nullable = false, name = "nights")
    private Integer nights;

    @Column(nullable = false, name = "days")
    private Integer days;

    @Column(length = 1000)
    private String profileImage;

    @Builder
    public TravelTemplateEntity(
        final TravelProgramEntity travelProgram,
        final String traveler,
        final String country,
        final String city,
        final String continent,
        final String weatherInfo,
        final String cultureInfo,
        final String foodInfo,
        final String thumbnail,
        final String link,
        final Long viewCount,
        final Integer budgetPerPerson,
        final String summary,
        final String title,
        final Integer nights,
        final Integer days,
        final String profileImage
    ) {
        this.travelProgram = travelProgram;
        this.traveler = traveler;
        this.country = country;
        this.city = city;
        this.continent = continent;
        this.weatherInfo = weatherInfo;
        this.cultureInfo = cultureInfo;
        this.foodInfo = foodInfo;
        this.thumbnail = thumbnail;
        this.link = link;
        this.viewCount = viewCount == null ? 0L : viewCount;
        this.budgetPerPerson = budgetPerPerson;
        this.summary = summary;
        this.title = title;
        this.nights = nights;
        this.days = days;
        this.profileImage = profileImage;
    }
}
