package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import com.kdt_y_be_toy_project2.domain.itinerary.dto.AccommodationInfoDTO;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryDTO;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class AccommodationInfo {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "accommodation_start_date_time")),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "accommodation_end_date_time"))
    })
    private TimeScheduleInfo accommodationSchedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "accommodation_place_name")),
    })
    private PlaceInfo accommodationPlaceInfo;

    public static AccommodationInfo to(AccommodationInfoDTO accommodationInfoDTO) {
        return AccommodationInfo.builder()
                .accommodationSchedule(TimeScheduleInfo.builder()
                        .startDateTime(accommodationInfoDTO.startDateTime())
                        .endDateTime(accommodationInfoDTO.endDateTime())
                        .build())
                .accommodationPlaceInfo(accommodationInfoDTO.accommodationPlaceInfo())
                .build();
    }


}
