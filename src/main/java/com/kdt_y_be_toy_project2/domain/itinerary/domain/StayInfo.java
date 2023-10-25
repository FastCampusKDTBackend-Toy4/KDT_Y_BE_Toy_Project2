package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import com.kdt_y_be_toy_project2.domain.itinerary.dto.MoveInfoDTO;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.StayInfoDTO;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private TimeScheduleInfo staySchedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "stay_place_name")),
    })
    private PlaceInfo stayPlaceInfo;

    public static StayInfo to(final StayInfoDTO stayInfoDTO) {
        return StayInfo.builder()
                .staySchedule(stayInfoDTO.staySchedule())
                .stayPlaceInfo(stayInfoDTO.stayPlaceInfo())
                .build();
    }
}
