package com.pentalog.api.exception;

import java.util.List;

import com.pentalog.api.dto.response.ApiError;

public class InvalidRequestException extends CustomExceptionContainingErrors {

	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String message) {
		super(message);
	}

	public InvalidRequestException(String message, List<ApiError> errors) {
		super(message, errors);
	}

	public InvalidRequestException(String message, ApiError error) {
		super(message, error);
	}
}
