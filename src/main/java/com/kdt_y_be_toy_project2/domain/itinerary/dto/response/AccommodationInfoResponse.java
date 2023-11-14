package com.kdt_y_be_toy_project2.domain.itinerary.dto.response;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder
public record AccommodationInfoResponse(
        @Schema(description = "숙박 시작 시간")
        String startDateTime,
        @Schema(description = "숙박 종료 시간")
        String endDateTime,
        @Schema(description = "숙박 장소")
        PlaceInfo accommodationPlaceName
) {

    public static AccommodationInfoResponse from(AccommodationInfo accommodationInfo) {
        return AccommodationInfoResponse.builder()
                .startDateTime(LocalDateTimeUtil.toString(accommodationInfo.getAccommodationSchedule().getStartDateTime()))
                .endDateTime(LocalDateTimeUtil.toString(accommodationInfo.getAccommodationSchedule().getEndDateTime()))
                .accommodationPlaceName(accommodationInfo.getAccommodationPlaceInfo())
                .build();
    }
}
