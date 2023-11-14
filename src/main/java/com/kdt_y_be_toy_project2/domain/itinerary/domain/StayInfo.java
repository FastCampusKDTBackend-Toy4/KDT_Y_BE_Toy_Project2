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
public class StayInfo {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "stay_start_date_time")),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "stay_end_date_time"))
    })
    private DateTimeScheduleInfo staySchedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "stay_place_name")),
            @AttributeOverride(name = "road_address_name", column = @Column(name = "stay_place_name_road_address_name")),
            @AttributeOverride(name = "x", column = @Column(name = "stay_place_name_x")),
            @AttributeOverride(name = "y", column = @Column(name = "stay_place_name_y")),
    })
    private PlaceInfo stayPlaceInfo;


}
