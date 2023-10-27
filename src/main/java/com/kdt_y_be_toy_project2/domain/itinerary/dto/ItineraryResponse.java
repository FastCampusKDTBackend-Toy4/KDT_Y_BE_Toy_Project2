package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.response.AccommodationInfoResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.response.MoveInfoResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.response.StayInfoResponse;
import lombok.Builder;

@Builder
public record ItineraryResponse (
        Long id,
        AccommodationInfoResponse accommodationInfoResponse,
        MoveInfoResponse moveInfoResponse,
        StayInfoResponse stayInfoResponse
) {

    public static ItineraryResponse from(final Itinerary itinerary) {
        return ItineraryResponse.builder()
                .id(itinerary.getId())
                .accommodationInfoResponse(AccommodationInfoResponse.from(itinerary.getAccommodationInfo()))
                .moveInfoResponse(MoveInfoResponse.from(itinerary.getMoveInfo()))
                .stayInfoResponse(StayInfoResponse.from(itinerary.getStayInfo()))
                .build();
    }

}
