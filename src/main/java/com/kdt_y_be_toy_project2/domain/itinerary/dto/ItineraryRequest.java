package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.AccommodationInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.MoveInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.StayInfoRequest;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "여정 요청")
public record ItineraryRequest(
        @Schema(description = "여가 계획")
        StayInfoRequest stayInfoRequest,
        @Schema(description = "이동 계획")
        MoveInfoRequest moveInfoRequest,
        @Schema(description = "숙박 계획")
        AccommodationInfoRequest accommodationInfoRequest)
{

    public static Itinerary toEntity(final ItineraryRequest itineraryRequest, Trip trip) {
        return Itinerary.builder().trip(trip).stayInfo(StayInfoRequest.to(itineraryRequest.stayInfoRequest())).moveInfo(MoveInfoRequest.to(itineraryRequest.moveInfoRequest())).accommodationInfo(AccommodationInfoRequest.to(itineraryRequest.accommodationInfoRequest())).build();
    }

}
