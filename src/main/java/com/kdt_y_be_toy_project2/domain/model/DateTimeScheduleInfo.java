package com.kdt_y_be_toy_project2.domain.model;

import com.kdt_y_be_toy_project2.domain.model.exception.InvalidDateRangeException;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DateTimeScheduleInfo {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Builder
    private DateTimeScheduleInfo(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validateDateTimeRange(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public boolean isIncludeInScheduleDateRange(LocalDateTime target) {
        return !target.isBefore(startDateTime) && !target.isAfter(endDateTime);
    }

    private void validateDateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) && isDayTrip(start, end)) {
            throw new InvalidDateRangeException("startDateTime 는 항상 endDateTime 보다 과거여야 합니다. "
                + "{\"startDate = \" %s / endDate = %s}".formatted(start, end));
        }
    }

    private boolean isDayTrip(LocalDateTime start, LocalDateTime end) {
        return !start.equals(end);
    }

    public void changeScheduleDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validateDateTimeRange(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
