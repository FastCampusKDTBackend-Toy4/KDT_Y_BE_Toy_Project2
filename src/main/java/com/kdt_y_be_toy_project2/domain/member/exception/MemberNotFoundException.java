package com.kdt_y_be_toy_project2.domain.member.exception;

import com.kdt_y_be_toy_project2.global.error.ApplicationException;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

public class MemberNotFoundException extends ApplicationException {

	private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_NOT_FOUND;

	public MemberNotFoundException() {
		super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
	}
}
