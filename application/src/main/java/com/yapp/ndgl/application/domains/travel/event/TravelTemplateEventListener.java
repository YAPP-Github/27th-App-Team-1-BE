package com.yapp.ndgl.application.domains.travel.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TravelTemplateEventListener {

    private final TravelTemplateDomainService travelTemplateDomainService;

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTravelTemplateViewCountEvent(final TravelTemplateViewCountEvent event) {
        travelTemplateDomainService.incrementViewCount(event.travelTemplateId());
    }
}
