package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.response.AccommodationInfoResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.response.MoveInfoResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.response.StayInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "여정 응답")
public record ItineraryResponse(
        @Schema(description = "여정 ID")
        Long id,
        @Schema(description = "여가 계획")
        StayInfoResponse stayInfoResponse,

        @Schema(description = "이동 계획")
        MoveInfoResponse moveInfoResponse,

        @Schema(description = "숙박 계획")
        AccommodationInfoResponse accommodationInfoResponse
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
