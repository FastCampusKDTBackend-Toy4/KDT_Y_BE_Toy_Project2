package com.kdt_y_be_toy_project2.domain.trip.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class TripSearchIllegalArgumentException extends ApplicationException {

	private final static ErrorCode ERROR_CODE = ErrorCode.TRIP_SEARCH_ILLEGAL_ARGUMENT;

	public TripSearchIllegalArgumentException() {
		super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
	}
}
