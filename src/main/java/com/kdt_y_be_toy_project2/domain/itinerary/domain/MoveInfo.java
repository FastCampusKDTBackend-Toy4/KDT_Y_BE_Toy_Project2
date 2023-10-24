package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import com.kdt_y_be_toy_project2.domain.itinerary.dto.MoveInfoDTO;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.persistence.*;

@Embeddable
public class MoveInfo {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "move_start_date_time", nullable = false)),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "move_end_date_time"))
    })
    private TimeScheduleInfo moveSchedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "soucre_place_name"))
    })
    private PlaceInfo sourcePlaceInfo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "dest_place_name"))
    })
    private PlaceInfo destPlaceInfo;


    @Column(name = "transportation_type")
    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;


    public static MoveInfo to(final MoveInfoDTO moveInfoDTO) {
        return MoveInfo.builder()
                .moveSchedule(moveInfoDTO.getMoveSchedule())
                .sourcePlaceInfo((moveInfoDTO.getSourcePlaceInfo()))
                .destPlaceInfo(moveInfoDTO.getDestPlaceInfo())
                .transportationType(moveInfoDTO.getTransportationType())
                .build();
    }

}
