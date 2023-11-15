package com.kdt_y_be_toy_project2.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// 날짜
	INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "날짜 범위가 잘못 선택되었습니다."),

	//사용자 권한
	INVALID_AUTH(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다."),

	// 여행
	TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "여행 정보를 찾을 수 없습니다."),
	TRIP_ALREADY_LIKES(HttpStatus.BAD_REQUEST, "이미 좋아요를 누르셨습니다."),
	TRIP_SEARCH_ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 여행 정보 검색 요청입니다."),

	// 여정
	ITINERARY_NOT_FOUND(HttpStatus.NOT_FOUND, "여정 정보를 찾을 수 없습니다."),
	INVALID_DATE_OR_TIME(HttpStatus.NOT_FOUND, "여정이 끝나는 시간이 여정의 시작 시간보다 빠를 수 없습니다."),
	INVALID_ITINERARY_DURATION(HttpStatus.NOT_FOUND, "여정 기간의 범위가 여행 기간의 범위를 벗어났습니다.");

	private final HttpStatus httpStatus;
	private final String simpleMessage;

	ErrorCode(HttpStatus httpStatus, String simpleMessage) {
		this.httpStatus = httpStatus;
		this.simpleMessage = simpleMessage;
	}
}
