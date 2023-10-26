package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;
import lombok.Builder;

import java.util.List;

@Builder
public record TripResponse(
        Long tripId,
        String tripName,
        String startDateTime,
        String endDateTime,
        TripType tripType,
        List<Itinerary> itineraries
) {
    public static TripResponse from(Trip trip) {
        return TripResponse.builder()
                .tripId(trip.getId())
                .tripName(trip.getName())
                .startDateTime(DateTimeUtil.toString(trip.getTripSchedule().getStartDateTime()))
                .endDateTime(DateTimeUtil.toString(trip.getTripSchedule().getEndDateTime()))
                .tripType(trip.getTripType())
                .itineraries(trip.getItineraries())
                .build();
    }
}
