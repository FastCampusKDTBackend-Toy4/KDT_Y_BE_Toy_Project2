package com.kdt_y_be_toy_project2.domain.model;

import com.kdt_y_be_toy_project2.domain.model.exception.InvalidDateRangeException;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DateTimeScheduleInfoTest {

    @DisplayName("TimeScheduleInfo 생성")
    @Nested
    class Context_Creation {

        @DisplayName("적절한 범위의 LocalDateTime 2개를 입력하면 통과한다.")
        @CsvSource(value = {
            "2023-11-10T00:00,2023-11-13T00:00",
            "2023-11-13T00:00,2023-11-13T00:01",
            "2023-11-13T23:59,2023-11-14T00:00",
            "2023-11-13T00:00,2023-11-13T00:00",    // 당일치기 허용
        })
        @ParameterizedTest(name = "[{index}] {0} ~ {1}")
        void withTwoLocalDateTime_willCreated(String startSample, String endSample) {

            // given
            LocalDateTime start = LocalDateTime.parse(startSample);
            LocalDateTime end = LocalDateTime.parse(endSample);

            // when
            DateTimeScheduleInfo dateTimeScheduleInfo = DateTimeScheduleInfo.builder().startDateTime(start)
                .endDateTime(end).build();

            // then
            Assertions.assertThat(dateTimeScheduleInfo.getStartDateTime()).isEqualTo(start);
            Assertions.assertThat(dateTimeScheduleInfo.getEndDateTime()).isEqualTo(end);
        }

        @DisplayName("startDate가 endDate 보다 미래라면, InvalidDateRangeException 예외가 던져진다.")
        @CsvSource(value = {
            "Year,2024-11-13T00:00,2023-11-13T00:00",
            "Month,2023-12-13T00:00,2023-11-13T00:00",
            "Day,2023-11-15T00:00,2023-11-13T00:00",
            "Hour,2023-11-13T01:00,2023-11-13T00:00",
            "Minute,2023-11-13T00:10,2023-11-13T00:00",
        })
        @ParameterizedTest(name = "[{index}] {0} 단위로 미래일 때")
        void startDate_isLaterThanEndDate_willThrowInvalidDateRangeException(String testTitle, String startSample, String endSample) {

            // given
            LocalDateTime start = LocalDateTime.parse(startSample);
            LocalDateTime end = LocalDateTime.parse(endSample);

            // when then
            Assertions.assertThatThrownBy(
                    () -> DateTimeScheduleInfo.builder().startDateTime(start).endDateTime(end).build())
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessage("startDateTime 는 항상 endDateTime 보다 과거여야 합니다. {"
                    + "\"startDate = \" %s / endDate = %s}".formatted(start, end));
        }
    }

    @DisplayName("isIncludeInScheduleDateRange()")
    @Nested
    class Context_isIncludeInScheduleDateRange {
        @DisplayName("검증하고자 하는 타겟이 일정 날짜에 포함된다면, true를 반환한다.")
        @ValueSource(strings = {
            "2023-11-10T00:00",
            "2023-11-10T00:01",
            "2023-11-10T10:00",
            "2023-11-10T23:59",
            "2023-11-11T00:00",
        })
        @ParameterizedTest
        void include_betweenStartDateAndEndDate_willReturnTrue(String target) {

            // given
            LocalDateTime start = LocalDateTime.parse("2023-11-10T00:00");
            LocalDateTime end = LocalDateTime.parse("2023-11-11T00:00");
            LocalDateTime targetLocalDateTime = LocalDateTime.parse(target);

            DateTimeScheduleInfo timeSceduleInfo = DateTimeScheduleInfo.builder().startDateTime(start)
                .endDateTime(end).build();

            // when
            boolean testResult = timeSceduleInfo
                .isIncludeInScheduleDateRange(targetLocalDateTime);

            // then
            Assertions.assertThat(testResult).isTrue();

        }

        @DisplayName("검증하고자 하는 타겟이 일정 날짜에 포함되지 않는다면, false를 반환한다.")
        @ValueSource(strings = {
            "2023-11-09T00:00",
            "2023-11-09T23:59",
            "2023-11-11T00:01",
            "2023-11-12T00:01",
        })
        @ParameterizedTest
        void notInclude_betweenStartDateAndEndDate_willReturnFalse(String target) {

            // given
            LocalDateTime start = LocalDateTime.parse("2023-11-10T00:00");
            LocalDateTime end = LocalDateTime.parse("2023-11-11T00:00");
            LocalDateTime targetLocalDateTime = LocalDateTime.parse(target);

            DateTimeScheduleInfo timeSceduleInfo = DateTimeScheduleInfo.builder().startDateTime(start)
                .endDateTime(end).build();

            // when
            boolean testResult = timeSceduleInfo
                .isIncludeInScheduleDateRange(targetLocalDateTime);

            // then
            Assertions.assertThat(testResult).isFalse();

        }
    }

    @DisplayName("changeScheduleDateRange()")
    @Nested
    class Context_changeScheduleDateRange {
        @DisplayName("적절한 범위의 LocalDateTime 2개를 입력하면 변경이 완료된다.")
        @CsvSource(value = {
            "2023-11-10T00:00,2023-11-13T00:00",
            "2023-11-13T00:00,2023-11-13T00:01",
            "2023-11-13T23:59,2023-11-14T00:00",
            "2023-11-13T00:00,2023-11-13T00:00",    // 당일치기 허용
        })
        @ParameterizedTest(name = "[{index}] {0} ~ {1}")
        void withTwoLocalDateTime_willCreated(String startSample, String endSample) {

            // given
            DateTimeScheduleInfo changeTarget = DateTimeScheduleInfo.builder()
                .startDateTime(LocalDateTime.of(2022, 1, 1, 0, 0))
                .endDateTime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .build();

            LocalDateTime start = LocalDateTime.parse(startSample);
            LocalDateTime end = LocalDateTime.parse(endSample);

            // when
            changeTarget.changeScheduleDateRange(start, end);

            // then
            Assertions.assertThat(changeTarget.getStartDateTime()).isEqualTo(start);
            Assertions.assertThat(changeTarget.getEndDateTime()).isEqualTo(end);
        }

        @DisplayName("startDate가 endDate 보다 미래라면, InvalidDateRangeException 예외가 던져진다.")
        @CsvSource(value = {
            "Year,2024-11-13T00:00,2023-11-13T00:00",
            "Month,2023-12-13T00:00,2023-11-13T00:00",
            "Day,2023-11-15T00:00,2023-11-13T00:00",
            "Hour,2023-11-13T01:00,2023-11-13T00:00",
            "Minute,2023-11-13T00:10,2023-11-13T00:00",
        })
        @ParameterizedTest(name = "[{index}] {0} 단위로 미래일 때")
        void startDate_isLaterThanEndDate_willThrowInvalidDateRangeException(String testTitle, String startSample, String endSample) {

            // given
            DateTimeScheduleInfo changeTarget = DateTimeScheduleInfo.builder()
                .startDateTime(LocalDateTime.of(2022, 1, 1, 0, 0))
                .endDateTime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .build();

            LocalDateTime start = LocalDateTime.parse(startSample);
            LocalDateTime end = LocalDateTime.parse(endSample);

            // when then
            Assertions.assertThatThrownBy(() -> changeTarget.changeScheduleDateRange(start, end))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessage("startDateTime 는 항상 endDateTime 보다 과거여야 합니다. {"
                    + "\"startDate = \" %s / endDate = %s}".formatted(start, end));
        }
    }
}
