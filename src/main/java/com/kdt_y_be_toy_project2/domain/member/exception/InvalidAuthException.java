package com.kdt_y_be_toy_project2.domain.member.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class InvalidAuthException extends ApplicationException {

	private final static ErrorCode ERROR_CODE = ErrorCode.INVALID_AUTH;

	public InvalidAuthException() {
		super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
	}
}
