package com.yapp.ndgl.common.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Transportation(
	TransportationMode mode,
	@JsonProperty("time_min") Integer timeMin
) {
}
