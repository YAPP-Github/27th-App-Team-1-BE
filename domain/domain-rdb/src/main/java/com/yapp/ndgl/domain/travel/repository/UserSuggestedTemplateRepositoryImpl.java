package com.yapp.ndgl.domain.travel.repository;

import static com.yapp.ndgl.domain.travel.entity.QUserSuggestedTemplateEntity.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSuggestedTemplateRepositoryImpl implements UserSuggestedTemplateRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserSuggestedTemplateEntity> findByStatus(
        final SuggestionStatus status,
        final Pageable pageable
    ) {
        BooleanBuilder where = new BooleanBuilder();
        if (status != null) {
            where.and(userSuggestedTemplateEntity.status.eq(status));
        }

        List<UserSuggestedTemplateEntity> content = queryFactory
            .selectFrom(userSuggestedTemplateEntity)
            .where(where)
            .orderBy(userSuggestedTemplateEntity.createdAt.desc(), userSuggestedTemplateEntity.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(userSuggestedTemplateEntity.count())
            .from(userSuggestedTemplateEntity)
            .where(where)
            .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }
}
