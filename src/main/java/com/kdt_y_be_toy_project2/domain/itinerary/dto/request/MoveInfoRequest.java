package com.kdt_y_be_toy_project2.domain.itinerary.dto.request;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;


@Builder
public record MoveInfoRequest(
        @NotNull(message = "출발 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @Pattern(regexp = LocalDateTimeUtil.LOCAL_DATE_TIME_REGEX, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25 12:30)")
        String startDateTime,

        @NotNull(message = "도착 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @Pattern(regexp = LocalDateTimeUtil.LOCAL_DATE_TIME_REGEX, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25 12:30)")
        String endDateTime,

        @NotBlank(message = "출발지를 입력해야 합니다.")
        String sourcePlaceName,

        @NotBlank(message = "도착지를 입력해야 합니다.")
        String destPlaceName,
        String transportationType
){
    public static MoveInfo to(final MoveInfoRequest moveInfoRequest) {
        return MoveInfo.builder()
                .moveSchedule(DateTimeScheduleInfo.builder()
                        .startDateTime(LocalDateTimeUtil.toLocalDateTime(moveInfoRequest.startDateTime()))
                        .endDateTime(LocalDateTimeUtil.toLocalDateTime(moveInfoRequest.endDateTime()))
                        .build())
                .sourcePlaceInfo(PlaceInfo.builder().name(moveInfoRequest.sourcePlaceName).build())
                .destPlaceInfo(PlaceInfo.builder().name(moveInfoRequest.destPlaceName).build())
                .transportationType(TransportationType.getByValue(moveInfoRequest.transportationType))
                .build();
    }
}
