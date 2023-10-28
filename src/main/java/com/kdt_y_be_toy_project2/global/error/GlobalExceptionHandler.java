package com.kdt_y_be_toy_project2.global.error;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
		log.error("ApplicationError : {}", ex.getMessage());
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
			.body(new ErrorResponse(LocalDateTime.now(), ex.getErrorCode().getSimpleMessage()));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(LocalDateTime.now(), "알 수 없는 오류입니다. 다음에 시도해주세요."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleInternalServerError(RuntimeException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(LocalDateTime.now(), "알 수 없는 오류입니다. 다음에 시도해주세요."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleBindValidationError(BindException ex) {
		log.error(ex.getMessage(), ex);
		List<String> messages = ex.getAllErrors().stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.toList();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(LocalDateTime.now(), String.join(",", messages)));
	}
}
