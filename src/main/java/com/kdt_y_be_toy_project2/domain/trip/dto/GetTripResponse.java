package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetTripResponse {

    private final Long tripId;
    private final String name;
    private final TimeScheduleInfo tripScheduleInfo;
    private final TripType tripType;

    @Builder
    private GetTripResponse(Long tripId, String name, TimeScheduleInfo tripScheduleInfo, TripType tripType) {
        this.tripId = tripId;
        this.name = name;
        this.tripScheduleInfo = tripScheduleInfo;
        this.tripType = tripType;
    }

    public static GetTripResponse from(Trip trip) {
        return GetTripResponse.builder()
                .tripId(trip.getId())
                .tripScheduleInfo(trip.getTripSchedule())
                .tripType(trip.getTripType())
                .build();
    }
}
