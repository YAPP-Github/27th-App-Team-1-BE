package com.yapp.ndgl.domain.travel.repository;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.ndgl.domain.travel.entity.QTravelTemplateEntity;
import com.yapp.ndgl.domain.travel.entity.TravelTemplateEntity;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TravelTemplateRepositoryImpl implements TravelTemplateRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TravelTemplateEntity> findRandomTemplates(
        final String country,
        final Pageable pageable
    ) {
        QTravelTemplateEntity travelTemplate = QTravelTemplateEntity.travelTemplateEntity;
        BooleanBuilder where = new BooleanBuilder();
        if (country != null) {
            where.and(travelTemplate.country.eq(country));
        }

        return queryFactory.selectFrom(travelTemplate)
            .where(where)
            .orderBy(Expressions.numberTemplate(Double.class, "rand()").asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }
}
