package com.yapp.ndgl.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransportationMode {
	DRIVING("일반 승용차 자차 운전"),
	TRANSIT("버스, 지하철, 기차 등 대중교통"),
	WALKING("도보 이동"),
	BICYCLING("자전거 이동"),
	TAXI("택시, 그랩, 우버 등 차량 공유 서비스"),
	TWO_WHEELER("오토바이(이륜차) 이동"),
	FERRY("배, 페리 등 수상 이동"),
	FLIGHT("비행기(항공편) 이동");

	private final String description;
}
