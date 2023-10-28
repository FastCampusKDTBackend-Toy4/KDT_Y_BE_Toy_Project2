package com.kdt_y_be_toy_project2.domain.itinerary.dto.response;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder
public record StayInfoResponse(
        @Schema(description = "여가 시작 시간")
        String startDateTime,
        @Schema(description = "여가 종료 시간")
        String endDateTime,
        @Schema(description = "여가 장소")
        String stayPlaceName
) {
    public static StayInfoResponse from(final StayInfo stayInfo) {
        return StayInfoResponse.builder()
                .startDateTime(LocalDateTimeUtil.toString(stayInfo.getStaySchedule().getStartDateTime()))
                .endDateTime(LocalDateTimeUtil.toString(stayInfo.getStaySchedule().getEndDateTime()))
                .stayPlaceName(stayInfo.getStayPlaceInfo().getName())
                .build();
    }
}
