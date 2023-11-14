package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
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
    private DateTimeScheduleInfo accommodationSchedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "accommodation_place_name")),
            @AttributeOverride(name = "road_address_name", column = @Column(name = "accommodation_road_address_name")),
            @AttributeOverride(name = "x", column = @Column(name = "accommodation_x")),
            @AttributeOverride(name = "y", column = @Column(name = "accommodation_y")),
    })
    private PlaceInfo accommodationPlaceInfo;
}
