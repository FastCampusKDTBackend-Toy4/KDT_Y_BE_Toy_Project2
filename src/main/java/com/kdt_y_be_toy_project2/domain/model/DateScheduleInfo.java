package com.kdt_y_be_toy_project2.domain.model;

import java.time.LocalDate;

import com.kdt_y_be_toy_project2.domain.model.exception.InvalidDateRangeException;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DateScheduleInfo {

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    private DateScheduleInfo(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isIncludeInScheduleDateRange(LocalDate target) {
        return !target.isBefore(startDate) && !target.isAfter(endDate);
    }

    private void validateDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end) && isDayTrip(start, end)) {
            throw new InvalidDateRangeException("startDate 는 항상 endDate 보다 과거여야 합니다. "
                + "{\"startDate = \" %s / endDate = %s}".formatted(start, end));
        }
    }

    private boolean isDayTrip(LocalDate start, LocalDate end) {
        return !start.equals(end);
    }

    public void changeScheduleDateRange(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
