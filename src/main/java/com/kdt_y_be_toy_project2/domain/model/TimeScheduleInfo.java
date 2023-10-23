package com.kdt_y_be_toy_project2.domain.model;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class TimeScheduleInfo {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Builder
    private TimeScheduleInfo(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
