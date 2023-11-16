package com.kdt_y_be_toy_project2.domain.itinerary.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class ApiConnectionException extends ApplicationException {
    private static final ErrorCode ERROR_CODE = ErrorCode.HTTP_CLIENT_CONNECTION_ERROR;

    public ApiConnectionException() {
        super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
    }
}
