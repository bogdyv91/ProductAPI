package com.pentalog.api.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pentalog.api.dto.response.ApiResponse;
import com.pentalog.api.exception.ImpossibleToConvertPriceException;
import com.pentalog.api.exception.InvalidRequestException;
import com.pentalog.api.exception.ObjectNotFoundException;

import lombok.extern.java.Log;

@ControllerAdvice
@Log
public class RestControllerAdvice {

	@ExceptionHandler(ImpossibleToConvertPriceException.class)
	public ResponseEntity<ApiResponse> handleImpossibleToConvertPriceException(ImpossibleToConvertPriceException ex) {
		log.warning(ex.getMessage());
		ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(),
				ex.getErrors());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<ApiResponse> handleObjectNotFoundException(ObjectNotFoundException ex) {
		log.warning(ex.getMessage());
		ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage(),
				ex.getErrors());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ApiResponse> handleInvalidRequestException(InvalidRequestException ex) {
		log.warning(ex.getMessage());
		ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(),
				ex.getErrors());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
