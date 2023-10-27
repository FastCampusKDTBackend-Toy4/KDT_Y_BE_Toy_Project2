package com.kdt_y_be_toy_project2.global.factory;

import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class DateTimeScheduleInfoTestFactory {

    public static DateTimeScheduleInfo createRandom() {
        return createRandom(LocalDateTime.now());
    }

    public static DateTimeScheduleInfo createRandom(LocalDateTime baseDateTime) {

        long randomDaysToAddForStartDate = ThreadLocalRandom.current().nextLong(365);
        LocalDateTime startDateTime = baseDateTime.minusDays(randomDaysToAddForStartDate);

        long randomDaysToAddForEndDate = ThreadLocalRandom.current().nextLong(365) + 1;
        LocalDateTime endDateTime = startDateTime.plusDays(randomDaysToAddForEndDate);

        return DateTimeScheduleInfo.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }
}
