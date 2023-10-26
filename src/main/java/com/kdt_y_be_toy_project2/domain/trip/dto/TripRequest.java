package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TripRequest(
        @NotBlank(message = "여행 이름을 입력해야 합니다.")
        String name,

        @NotNull(message = "여행 시작 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @NotNull(message = "여행 종료 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @NotNull(message = "국내외여부를 입력해야 합니다. (예: DOMESTIC)")
        TripType tripType
) {
    public static Trip toEntity(final TripRequest request) {
        return Trip.builder()
                .name(request.name)
                .tripType(request.tripType)
                .tripSchedule(
                        DateScheduleInfo.builder()
                                .startDate(LocalDate.from(request.startDate))
                                .endDate(LocalDate.from(request.endDate))
                                .build()
                ).build();
    }
}
