package com.kdt_y_be_toy_project2.domain.itinerary.dto.response;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import lombok.Builder;


@Builder
public record StayInfoResponse(

        String startDateTime,
        String endDateTime,
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
