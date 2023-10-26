package com.kdt_y_be_toy_project2.domain.trip.domain;

import lombok.Getter;

@Getter
public enum TripType {
    DOMESTIC("국내"), FOREIGN("국외");

    private final String value;

    TripType(String value) {
        this.value = value;
    }
}
