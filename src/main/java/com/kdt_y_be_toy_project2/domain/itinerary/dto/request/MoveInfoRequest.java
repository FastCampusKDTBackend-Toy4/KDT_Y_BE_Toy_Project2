package com.kdt_y_be_toy_project2.domain.itinerary.dto.request;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;


@Builder
public record MoveInfoRequest(
        @NotNull(message = "출발 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @Pattern(regexp = LocalDateTimeUtil.LOCAL_DATE_TIME_REGEX, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25 12:30)")
        @Schema(description = "출발 시간")
        String startDateTime,

        @NotNull(message = "도착 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @Pattern(regexp = LocalDateTimeUtil.LOCAL_DATE_TIME_REGEX, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25 12:30)")
        @Schema(description = "도착 시간")
        String endDateTime,

        @NotBlank(message = "출발 장소를 입력해야 합니다.")
        @Schema(description = "출발지")
        String sourcePlaceName,

        @NotBlank(message = "도착 장소를 입력해야 합니다.")
        @Schema(description = "도착지")
        String destPlaceName,
        @Schema(description = "이동수단")
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
