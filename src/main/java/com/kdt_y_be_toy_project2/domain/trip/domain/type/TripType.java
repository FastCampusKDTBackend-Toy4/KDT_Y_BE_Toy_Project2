package com.kdt_y_be_toy_project2.domain.trip.domain.type;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum TripType {
	DOMESTIC("국내"), FOREIGN("국외");

	private final String value;

	TripType(String value) {
		this.value = value;
	}

	public static TripType getByValue(String value) {
		return Arrays.stream(TripType.values())
			.filter(val -> val.getValue().equals(value))
			.findFirst()
			.orElseThrow();
	}
}
