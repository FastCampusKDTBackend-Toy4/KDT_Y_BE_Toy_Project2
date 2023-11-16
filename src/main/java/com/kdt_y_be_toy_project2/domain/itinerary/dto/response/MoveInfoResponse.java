package com.kdt_y_be_toy_project2.domain.itinerary.dto.response;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder
public record MoveInfoResponse(
        @Schema(description = "출발 시간")
        String startDateTime,
        @Schema(description = "도착 시간")
        String endDateTime,
        @Schema(description = "출발지")
        PlaceInfo sourcePlaceName,
        @Schema(description = "도착지")
        PlaceInfo destPlaceName,
        @Schema(description = "이동수단")
        String transportationType
) {
    public static MoveInfoResponse from(final MoveInfo moveInfo) {

        return MoveInfoResponse.builder()
                .startDateTime(LocalDateTimeUtil.toString(moveInfo.getMoveSchedule().getStartDateTime()))
                .endDateTime(LocalDateTimeUtil.toString(moveInfo.getMoveSchedule().getEndDateTime()))
                .sourcePlaceName(moveInfo.getSourcePlaceInfo())
                .destPlaceName(moveInfo.getDestPlaceInfo())
                .transportationType(moveInfo.getTransportationType().getValue())
                .build();
    }
}
