package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import lombok.Builder;

@Builder
public record GetTripResponse(
        Long tripId,
        String name,
        TimeScheduleInfo tripScheduleInfo,
        TripType tripType
) {
    public static GetTripResponse from(Trip trip) {
        return GetTripResponse.builder()
                .tripId(trip.getId())
                .tripScheduleInfo(trip.getTripSchedule())
                .tripType(trip.getTripType())
                .build();
    }
}
