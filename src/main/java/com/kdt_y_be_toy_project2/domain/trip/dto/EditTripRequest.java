package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record EditTripRequest(
        @NotBlank
        String name,

        @NotNull
        @DateTimeFormat(pattern = "YYYY-MM-DD HH:mm")
        LocalDateTime startDateTime,

        @NotNull
        @DateTimeFormat(pattern = "YYYY-MM-DD HH:mm")
        LocalDateTime endDateTime,

        @NotNull
        TripType tripType
) {
    public static Trip toEntity(EditTripRequest request) {
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
