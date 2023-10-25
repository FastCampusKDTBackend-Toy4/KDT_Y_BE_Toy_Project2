package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Builder
public record MoveInfoDTO (
        @NotNull(message = "이동 시작 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startDateTime,

        @NotNull(message = "이동 종료 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endDateTime,

        PlaceInfo sourcePlaceInfo,

        PlaceInfo destPlaceInfo

        , TransportationType transportationType
){
    public static MoveInfoDTO from(final MoveInfo moveInfo){
        return MoveInfoDTO.builder()
                .startDateTime(moveInfo.getMoveSchedule().getStartDateTime())
                .endDateTime(moveInfo.getMoveSchedule().getEndDateTime())
                .sourcePlaceInfo(moveInfo.getSourcePlaceInfo())
                .destPlaceInfo(moveInfo.getDestPlaceInfo())
                .transportationType(moveInfo.getTransportationType())
                .build();
    }
}
