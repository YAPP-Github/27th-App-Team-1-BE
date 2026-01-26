package com.yapp.ndgl.application.domains.travel.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelIdResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;
import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.service.TravelTemplateDomainService;
import com.yapp.ndgl.domain.travel.service.UserTravelDomainService;
import com.yapp.ndgl.domain.travel.service.UserTravelPlaceDomainService;
import com.yapp.ndgl.domain.user.User;
import com.yapp.ndgl.domain.user.service.UserDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTravelService {

	private final UserTravelDomainService userTravelDomainService;
	private final TravelTemplateDomainService travelTemplateDomainService;
    private final UserTravelPlaceDomainService userTravelPlaceDomainService;
	private final UserDomainService userDomainService;

	@Transactional
	public UserTravelIdResponse createUserTravel(final String uuid, final CreateUserTravelRequest request) {
		User user = userDomainService.findByUuid(uuid);

		TravelTemplate template = travelTemplateDomainService.findById(request.templateId());

		long daysBetween = ChronoUnit.DAYS.between(request.startDate(), request.endDate());
		int nights = (int)daysBetween;
		int days = nights + 1;

		if (nights < template.getNights() || days < template.getDays()) {
			throw new GlobalException(TravelErrorCode.INVALID_TRAVEL_DATE_RANGE);
		}

        UserTravel userTravel = userTravelDomainService.createUserTravel(user, template, request.startDate(),
            request.endDate(), nights, days);

        List<TravelTemplatePlace> templatePlaces = travelTemplateDomainService.findPlacesByTravelTemplateId(
			template.getId());

        userTravelPlaceDomainService.createUserTravelPlaces(templatePlaces, userTravel.getTemplateId());

		return new UserTravelIdResponse(userTravel.getId());
	}
}
