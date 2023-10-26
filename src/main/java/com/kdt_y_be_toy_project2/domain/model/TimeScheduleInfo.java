package com.kdt_y_be_toy_project2.domain.model;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripValidException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class TimeScheduleInfo {

    @NotNull(message = "여행 시작 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @NotNull(message = "여행 종료 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    @Builder
    private TimeScheduleInfo(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void isDateTimeValid() {
        LocalDateTime now = LocalDateTime.now();
        if (startDateTime.isBefore(now) || endDateTime.isBefore(now)) {
            throw new TripValidException(ErrorCode.TRIP_NOT_BEFORE_CURRENT);
        }

        if (startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime)) {
            throw new TripValidException(ErrorCode.TRIP_NOT_ENDDATETIME_AFTER_STARTDATETIME);
        }
    }
}
