package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record TripRequest(
        @NotBlank(message = "여행 이름을 입력해야 합니다.")
        String name,

        @NotNull(message = "여행 시작 시간을 입력해야 합니다.")
        @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)")
        String startDateTime,

        @NotNull(message = "여행 종료 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)")
        String endDateTime,

        @NotNull(message = "국내외여부를 입력해야 합니다. (예: DOMESTIC)")
        TripType tripType
) {
    public static Trip toEntity(final TripRequest request) {
        return Trip.builder()
                .name(request.name)
                .tripType(request.tripType)
                .tripSchedule(
                        TimeScheduleInfo.builder()
                                .startDateTime(DateTimeUtil.toLocalDateTime(request.startDateTime))
                                .endDateTime(DateTimeUtil.toLocalDateTime(request.endDateTime))
                                .build()
                ).build();
    }
}
