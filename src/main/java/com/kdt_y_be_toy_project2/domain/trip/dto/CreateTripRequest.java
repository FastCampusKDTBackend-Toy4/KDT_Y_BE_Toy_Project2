package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateTripRequest(
        @NotBlank(message = "여행 이름을 입력해야 합니다.")
        String name,

        @NotNull(message = "여행 시작 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startDateTime,

        @NotNull(message = "여행 종료 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endDateTime,

        @NotNull(message = "국내외여부를 입력해야 합니다. (예: DOMESTIC)")
        TripType tripType
) {
    public static Trip toEntity(final CreateTripRequest request) {
        return Trip.builder()
                .name(request.name)
                .tripType(request.tripType)
                .tripSchedule(
                        TimeScheduleInfo.builder()
                                .startDateTime(LocalDateTime.from(request.startDateTime))
                                .endDateTime(LocalDateTime.from(request.endDateTime))
                                .build()
                ).build();
    }
}
