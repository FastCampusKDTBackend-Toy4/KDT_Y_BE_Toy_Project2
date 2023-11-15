package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum TransportationType {
	//도보, 자동차, 대중교통
	WALK("걷기"), CAR("차"), PUBLIC_TRANSPORT("대중교통");

	private final String value;

	TransportationType(String value) {
		this.value = value;
	}

	public static TransportationType getByValue(String value) {
		return Arrays.stream(TransportationType.values())
			.filter(val -> val.getValue().equals(value))
			.findFirst()
			.orElseThrow(() -> new NullPointerException("이동수단에는 [걷기, 차, 대중교통] 만 입력이 가능합니다."));
	}
}
