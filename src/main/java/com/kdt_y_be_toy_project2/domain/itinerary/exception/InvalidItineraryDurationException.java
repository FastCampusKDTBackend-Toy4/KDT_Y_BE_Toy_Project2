package com.kdt_y_be_toy_project2.domain.itinerary.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class InvalidItineraryDurationException extends ApplicationException {
    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_ITINERARY_DURATION;
    public InvalidItineraryDurationException() {
        super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
    }
}
