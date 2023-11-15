package com.kdt_y_be_toy_project2.domain.itinerary.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class InvalidAuthException extends ApplicationException {

    public InvalidAuthException() {
        super(ErrorCode.INVALID_AUTH.getSimpleMessage(), ErrorCode.INVALID_AUTH);
    }
}
