package com.yapp.ndgl.domain.travel.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
    name = "user_travels",
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_template_id", columnList = "template_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTravelEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "nights", nullable = false)
    private Integer nights;

    @Column(name = "days", nullable = false)
    private Integer days;

    @Builder
    public UserTravelEntity(
        final Long userId,
        final Long templateId,
        final String title,
        final String country,
        final String city,
        final LocalDate startDate,
        final LocalDate endDate,
        final Integer nights,
        final Integer days
    ) {
        this.userId = userId;
        this.templateId = templateId;
        this.title = title;
        this.country = country;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nights = nights;
        this.days = days;
    }
}
