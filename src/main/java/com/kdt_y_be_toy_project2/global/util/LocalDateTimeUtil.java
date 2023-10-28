package com.kdt_y_be_toy_project2.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    public final static String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

    public final static String LOCAL_DATE_TIME_REGEX = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9])";

    public static String toString(LocalDateTime dateTimeObject) {
        return dateTimeObject.format(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN));
    }

    public static LocalDateTime toLocalDateTime(String datetimeString) {
        return LocalDateTime.parse(datetimeString, DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN));
    }
}
