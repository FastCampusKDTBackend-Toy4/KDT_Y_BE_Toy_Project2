package com.kdt_y_be_toy_project2.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public final static String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

    public final static String LOCAL_DATE_REGEX_PATTERN = "(19|20)\\d{2}(\\/|-)(0[1-9]|1[1,2])(\\/|-)(0[1-9]|[12][0-9]|3[01])";

    public static String toString(LocalDate dateTimeObject) {
        return dateTimeObject.format(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN));
    }

    public static LocalDate toLocalDateTime(String datetimeString) {
        return LocalDate.parse(datetimeString, DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN));
    }
}
