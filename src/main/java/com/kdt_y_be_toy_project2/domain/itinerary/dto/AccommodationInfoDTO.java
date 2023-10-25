package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import lombok.Builder;


@Builder
public record AccommodationInfoDTO(TimeScheduleInfo accommodationSchedule, PlaceInfo accommodationPlaceInfo) {
    public static AccommodationInfoDTO from(final AccommodationInfo accommodationInfo) {
        return AccommodationInfoDTO.builder()
                .accommodationSchedule(accommodationInfo.getAccommodationSchedule())
                .accommodationPlaceInfo(accommodationInfo.getAccommodationPlaceInfo())
                .build();
    }
}
