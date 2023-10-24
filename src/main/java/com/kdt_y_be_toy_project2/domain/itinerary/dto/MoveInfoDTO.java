package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@AllArgsConstructor (access = AccessLevel.PROTECTED)
@Setter
public class MoveInfoDTO {

    @NotNull
    private TimeScheduleInfo moveSchedule;

    private PlaceInfo sourcePlaceInfo ;

    private PlaceInfo destPlaceInfo ;

    @NotNull
    private TransportationType transportationType;

    public static MoveInfoDTO from(final MoveInfo moveInfo){
        return MoveInfoDTO.builder()
                .moveSchedule(moveInfo.getMoveSchedule())
                .sourcePlaceInfo(moveInfo.getSourcePlaceInfo())
                .destPlaceInfo(moveInfo.getDestPlaceInfo())
                .transportationType(moveInfo.getTransportationType())
                .build();
    }
}
