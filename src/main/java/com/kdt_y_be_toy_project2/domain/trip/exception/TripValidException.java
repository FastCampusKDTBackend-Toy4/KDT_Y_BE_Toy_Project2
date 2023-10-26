package com.kdt_y_be_toy_project2.domain.trip.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class TripValidException extends ApplicationException {

	public TripValidException(ErrorCode errorCode) {
		super(errorCode.getSimpleMessage(), errorCode);
	}
}
