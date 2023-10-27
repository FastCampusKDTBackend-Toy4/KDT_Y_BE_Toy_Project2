package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.AccommodationInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.MoveInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.StayInfoRequest;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import lombok.Builder;

@Builder
public record ItineraryRequest(
        StayInfoRequest stayInfoRequest,
        MoveInfoRequest moveInfoRequest,
        AccommodationInfoRequest accommodationInfoRequest
) {

    public static Itinerary toEntity(final ItineraryRequest itineraryRequest, Trip trip) {
        return Itinerary.builder()
                .trip(trip)
                .stayInfo(StayInfoRequest.to(itineraryRequest.stayInfoRequest()))
                .moveInfo(MoveInfoRequest.to(itineraryRequest.moveInfoRequest()))
                .accommodationInfo(AccommodationInfoRequest.to(itineraryRequest.accommodationInfoRequest()))
                .build();
    }

}
