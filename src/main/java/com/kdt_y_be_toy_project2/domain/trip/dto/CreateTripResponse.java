package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;

import java.time.LocalDateTime;

public record CreateTripResponse(
        Long id,
        String name,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        TripType tripType
) {
}
