package com.kdt_y_be_toy_project2.domain.model.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class InvalidDateRangeException extends ApplicationException {

    public InvalidDateRangeException(String message) {
        super(message, ErrorCode.INVALID_DATE_RANGE);
    }
}
