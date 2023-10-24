package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import com.kdt_y_be_toy_project2.domain.itinerary.dto.AccommodationInfoDTO;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.persistence.*;

@Embeddable
public class AccommodationInfo {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "accommodation_start_date_time", nullable = false)),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "accommodation_end_date_time"))
    })
    private TimeScheduleInfo accommodationSchedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "accommodation_place_name", nullable = false)),
    })
    private PlaceInfo accommodationPlaceInfo;


}
