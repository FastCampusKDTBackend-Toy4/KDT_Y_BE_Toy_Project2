package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EditTripResponse(
        Long id,
        String name,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        TripType tripType
) {
    public static EditTripResponse from(Trip trip) {
        return EditTripResponse.builder()
                .id(trip.getId())
                .name(trip.getName())
                .startDateTime(trip.getTripSchedule().getStartDateTime())
                .endDateTime(trip.getTripSchedule().getEndDateTime())
                .tripType(trip.getTripType())
                .build();
    }
}
