package com.kdt_y_be_toy_project2.global.factory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;

public class DateScheduleInfoTestFactory {

    public static DateScheduleInfo createRandom() {
        return createRandom(LocalDate.now());
    }

    public static DateScheduleInfo createRandom(LocalDate baseDateTime) {

        long randomDaysToAddForStartDate = ThreadLocalRandom.current().nextLong(365);
        LocalDate startDate = baseDateTime.minusDays(randomDaysToAddForStartDate);

        long randomDaysToAddForEndDate = ThreadLocalRandom.current().nextLong(365) + 1;
        LocalDate endDate = startDate.plusDays(randomDaysToAddForEndDate);

        return DateScheduleInfo.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static DateTimeScheduleInfo createRandom2() {
        return createRandom2(LocalDateTime.now());
    }

    public static DateTimeScheduleInfo createRandom2(LocalDateTime baseDateTime) {

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
