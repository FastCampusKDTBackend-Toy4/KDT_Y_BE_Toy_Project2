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
            @AttributeOverride(name = "name", column = @Column(name = "move_soucre_place_name", nullable = false))
    })
    private PlaceInfo movePlaceSourceInfo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "move_dest_place_name", nullable = false))
    })
    private PlaceInfo movePlaceDestInfo;


    @Column(name = "transportation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;


    public static MoveInfo to(final MoveInfoDTO moveInfoDTO) {
        return MoveInfo.builder()
                .moveSchedule(moveInfoDTO.getMoveSchedule())
                .movePlaceDestInfo((moveInfoDTO.getMovePlaceDestInfo()))
                .movePlaceSourceInfo(moveInfoDTO.getMovePlaceSourceInfo())
                .transportationType(moveInfoDTO.getTransportationType())
                .build();
    }

}
