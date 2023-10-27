package com.kdt_y_be_toy_project2.domain.itinerary.dto.response;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import lombok.Builder;


@Builder
public record MoveInfoResponse(
        String startDateTime,
        String endDateTime,
        String sourcePlaceName,
        String destPlaceName,
        String transportationType
) {
    public static MoveInfoResponse from(final MoveInfo moveInfo) {
        return MoveInfoResponse.builder()
                .startDateTime(LocalDateTimeUtil.toString(moveInfo.getMoveSchedule().getStartDateTime()))
                .endDateTime(LocalDateTimeUtil.toString(moveInfo.getMoveSchedule().getEndDateTime()))
                .sourcePlaceName(moveInfo.getSourcePlaceInfo().getName())
                .destPlaceName(moveInfo.getDestPlaceInfo().getName())
                .transportationType(moveInfo.getTransportationType().getValue())
                .build();
    }
}
