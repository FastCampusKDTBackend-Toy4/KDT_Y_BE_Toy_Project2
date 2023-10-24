package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@AllArgsConstructor (access = AccessLevel.PROTECTED)
@Setter
public class MoveInfoDTO {

    @NotNull
    private TimeScheduleInfo moveSchedule;

    @NotNull
    private PlaceInfo movePlaceSourceInfo;

    @NotNull
    private PlaceInfo movePlaceDestInfo;

    @NotNull
    private TransportationType transportationType;

    public static MoveInfoDTO from(final MoveInfo moveInfo){
        return MoveInfoDTO.builder()
                .moveSchedule(moveInfo.getMoveSchedule())
                .movePlaceDestInfo(moveInfo.getMovePlaceDestInfo())
                .movePlaceSourceInfo(moveInfo.getMovePlaceSourceInfo())
                .transportationType(moveInfo.getTransportationType())
                .build();
    }
}
