package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class AccommodationInfoDTO {

    @NotNull
    private TimeScheduleInfo accommodationSchedule;

    @NotNull
    private PlaceInfo accommodationPlaceInfo;

    public static AccommodationInfoDTO from(final AccommodationInfo accommodationInfo) {
        return AccommodationInfoDTO.builder()
                .accommodationSchedule(accommodationInfo.getAccommodationSchedule())
                .accommodationPlaceInfo(accommodationInfo.getAccommodationPlaceInfo())
                .build();
    }
}
