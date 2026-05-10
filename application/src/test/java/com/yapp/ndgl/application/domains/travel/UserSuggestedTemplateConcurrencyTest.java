package com.yapp.ndgl.application.domains.travel;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.yapp.ndgl.application.config.TestDatabaseConfig;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.facade.UserSuggestedTemplateFacade;
import com.yapp.ndgl.application.executor.ConcurrencyExecutor;
import com.yapp.ndgl.common.type.DomesticRegion;
import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.common.type.TravelCategory;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateEntity;
import com.yapp.ndgl.domain.travel.repository.UserSuggestedTemplateRepository;

@SpringBootTest
@DisplayName("UserSuggestedTemplate 분산락 동시성 테스트")
class UserSuggestedTemplateConcurrencyTest extends TestDatabaseConfig {

    @Autowired
    private UserSuggestedTemplateFacade facade;

    @Autowired
    private UserSuggestedTemplateRepository repository;

    private static final String TEST_VIDEO_LINK = "https://youtu.be/concurTest1234";

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("100명이 동시에 같은 영상을 제안하면 PENDING 레코드는 1건만 생성된다")
    void 동시_100명_같은_영상_제안_시_1건만_생성() throws InterruptedException {
        // given
        CreateUserSuggestedTemplateRequest request = new CreateUserSuggestedTemplateRequest(
            TEST_VIDEO_LINK,
            "동시성 테스트 추천 이유",
            TravelCategory.UNCATEGORIZED,
            DomesticRegion.UNDEFINED
        );

        AtomicInteger successCount = new AtomicInteger(0);

        // when
        ConcurrencyExecutor.
            execute(100, 32, () -> {
            String uuid = UUID.randomUUID().toString();
            facade.createUserSuggestedTemplate(uuid, request);
            successCount.incrementAndGet();
        });

        // then
        List<UserSuggestedTemplateEntity> pendingTemplates = repository.findAll().stream()
            .filter(t -> t.getStatus() == SuggestionStatus.PENDING)
            .toList();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(pendingTemplates).hasSize(1);
    }
}
