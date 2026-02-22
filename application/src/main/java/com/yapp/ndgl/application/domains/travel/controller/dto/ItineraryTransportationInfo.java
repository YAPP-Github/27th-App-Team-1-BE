package com.yapp.ndgl.application.domains.travel.controller.dto;

import com.yapp.ndgl.common.type.TransportationMode;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItineraryTransportationInfo(
	@Schema(description = "교통수단", example = "TAXI", requiredMode = Schema.RequiredMode.REQUIRED)
	TransportationMode mode,
	@Schema(description = "소요 시간 (분)", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
	Integer timeMin
) {
}
