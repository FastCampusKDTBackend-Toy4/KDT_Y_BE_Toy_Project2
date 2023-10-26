package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import lombok.Builder;

@Builder
public record ItineraryResponse(Long id, StayInfo stayInfo, MoveInfo moveInfo, AccommodationInfo accommodationInfo) {
    public static ItineraryResponse from(final Itinerary itinerary) {
        return ItineraryResponse.builder()
                .id(itinerary.getId())
                .stayInfo(itinerary.getStayInfo())
                .moveInfo(itinerary.getMoveInfo())
                .accommodationInfo(itinerary.getAccommodationInfo())
                .build();
    }

}
