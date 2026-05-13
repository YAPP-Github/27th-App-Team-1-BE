package com.yapp.ndgl.application.domains.travel.event.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.yapp.ndgl.application.domains.travel.event.TravelTemplateViewCountEvent;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TravelTemplateEventListener {

    private final TravelTemplateDomainService travelTemplateDomainService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTravelTemplateViewCountEvent(final TravelTemplateViewCountEvent event) {
        try {
            travelTemplateDomainService.incrementViewCount(event.travelTemplateId());
        } catch (Exception e) {
            log.error("여행 템플릿 조회수 증가에 실패했습니다. travelTemplateId={}", event.travelTemplateId(), e);
        }
    }
}
