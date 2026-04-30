package com.yapp.ndgl.common.type;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Transportation(
	TransportationMode mode,
	@JsonAlias("time_min") Integer timeMin
) {
}
