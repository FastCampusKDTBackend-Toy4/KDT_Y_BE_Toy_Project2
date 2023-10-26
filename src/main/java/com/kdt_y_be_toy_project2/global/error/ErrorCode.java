package com.kdt_y_be_toy_project2.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 여행 관련 에러
    TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "여행 정보를 찾을 수 없습니다."),
    TRIP_NOT_BEFORE_CURRENT(HttpStatus.UNPROCESSABLE_ENTITY, "여행 출발날짜와 도착날짜는 현재날짜 이후이어야 합니다."),
    TRIP_NOT_ENDDATETIME_AFTER_STARTDATETIME(HttpStatus.UNPROCESSABLE_ENTITY, "여행 출발날짜는 도착날짜 이전이어야 합니다.");

    // 여정 관련 에러

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    ErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}
