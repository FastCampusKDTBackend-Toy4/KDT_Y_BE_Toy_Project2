package com.kdt_y_be_toy_project2.domain.itinerary.dto.response;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import lombok.Builder;


@Builder
public record AccommodationInfoResponse(
        String startDateTime,
        String endDateTime,
        String accommodationPlaceName
) {

    public static AccommodationInfoResponse from(AccommodationInfo accommodationInfo) {
        return AccommodationInfoResponse.builder()
                .startDateTime(LocalDateTimeUtil.toString(accommodationInfo.getAccommodationSchedule().getStartDateTime()))
                .endDateTime(LocalDateTimeUtil.toString(accommodationInfo.getAccommodationSchedule().getEndDateTime()))
                .accommodationPlaceName(accommodationInfo.getAccommodationPlaceInfo().getName())
                .build();
    }
}
