package com.kdt_y_be_toy_project2.domain.itinerary.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class TripNotFoundException extends ApplicationException {

    private final static ErrorCode ERROR_CODE = ErrorCode.TRIP_NOT_FOUND;

    public TripNotFoundException() {
        super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
    }
}
