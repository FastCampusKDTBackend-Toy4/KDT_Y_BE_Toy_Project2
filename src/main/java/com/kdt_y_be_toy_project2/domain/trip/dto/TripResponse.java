package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import lombok.Builder;

import java.util.List;

@Builder
public record TripResponse(
        Long tripId,
        String tripName,
        DateTimeScheduleInfo tripScheduleInfo,
        TripType tripType,
        List<Itinerary> itineraries
) {
    public static TripResponse from(Trip trip) {
        return TripResponse.builder()
                .tripId(trip.getId())
                .tripName(trip.getName())
                .tripScheduleInfo(trip.getTripSchedule())
                .tripType(trip.getTripType())
                .itineraries(trip.getItineraries())
                .build();
    }
}
