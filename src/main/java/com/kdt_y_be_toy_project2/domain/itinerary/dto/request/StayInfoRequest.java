package com.kdt_y_be_toy_project2.domain.itinerary.dto.request;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;


@Builder
public record StayInfoRequest(
        @NotNull(message = "휴식 시작 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @Pattern(regexp = LocalDateTimeUtil.LOCAL_DATE_TIME_REGEX, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25 12:30)")
        String startDateTime,

        @NotNull(message = "휴식 종료 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @Pattern(regexp = LocalDateTimeUtil.LOCAL_DATE_TIME_REGEX, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25 12:30)")
        String endDateTime,

        @NotBlank(message = "휴식지를 입력해야 합니다.")
        String stayPlaceName
) {
    public static StayInfo to(final StayInfoRequest stayInfoRequest) {
        return StayInfo.builder()
                .staySchedule(DateTimeScheduleInfo.builder()
                        .startDateTime(LocalDateTimeUtil.toLocalDateTime(stayInfoRequest.startDateTime()))
                        .endDateTime(LocalDateTimeUtil.toLocalDateTime(stayInfoRequest.endDateTime()))
                        .build())
                .stayPlaceInfo(PlaceInfo.builder().name(stayInfoRequest.stayPlaceName).build())
                .build();
    }
}
