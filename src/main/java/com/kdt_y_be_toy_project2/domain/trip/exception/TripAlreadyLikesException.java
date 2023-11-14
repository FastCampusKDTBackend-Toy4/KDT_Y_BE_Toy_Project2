package com.kdt_y_be_toy_project2.domain.trip.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class TripAlreadyLikesException extends ApplicationException {

	private final static ErrorCode ERROR_CODE = ErrorCode.TRIP_ALREADY_LIKES;

	public TripAlreadyLikesException() {
		super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
	}
}
