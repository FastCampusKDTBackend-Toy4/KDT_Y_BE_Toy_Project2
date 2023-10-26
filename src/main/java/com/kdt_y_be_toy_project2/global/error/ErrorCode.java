package com.kdt_y_be_toy_project2.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "여행 정보를 찾을 수 없습니다."),

    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "날짜 범위가 잘못 선택되었습니다.");

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    ErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}
