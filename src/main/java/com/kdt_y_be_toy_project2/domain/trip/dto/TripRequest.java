package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TripRequest(
        @NotBlank(message = "여행 이름을 입력해야 합니다.")
        String name,

        @Embedded
        TimeScheduleInfo timeScheduleInfo,

        @NotNull(message = "국내외여부를 입력해야 합니다. (예: DOMESTIC)")
        TripType tripType
) {
    public static Trip toEntity(final TripRequest request) {
        return Trip.builder()
                .name(request.name)
                .tripType(request.tripType)
                .tripSchedule(request.timeScheduleInfo)
                .build();
    }
}
