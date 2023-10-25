package com.kdt_y_be_toy_project2.global.error;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{

    private final ErrorCode errorCode;

    protected ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
