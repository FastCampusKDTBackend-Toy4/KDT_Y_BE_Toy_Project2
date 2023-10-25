package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Builder
public record MoveInfoRequest(
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
    public static MoveInfo to(final MoveInfoRequest moveInfoRequest) {
        return MoveInfo.builder()
                .moveSchedule(TimeScheduleInfo.builder()
                        .startDateTime(moveInfoRequest.startDateTime())
                        .endDateTime(moveInfoRequest.endDateTime())
                        .build())
                .sourcePlaceInfo((moveInfoRequest.sourcePlaceInfo()))
                .destPlaceInfo(moveInfoRequest.destPlaceInfo())
                .transportationType(moveInfoRequest.transportationType())
                .build();
    }
}
