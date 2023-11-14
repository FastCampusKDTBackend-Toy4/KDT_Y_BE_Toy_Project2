package com.kdt_y_be_toy_project2.global.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class BadTokenException extends AuthenticationException {

    public BadTokenException(String message) {
        super(message);
    }
}
