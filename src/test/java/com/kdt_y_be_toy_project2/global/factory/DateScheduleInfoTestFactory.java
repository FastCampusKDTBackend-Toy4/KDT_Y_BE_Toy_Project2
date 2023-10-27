package com.kdt_y_be_toy_project2.global.factory;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;

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
}
