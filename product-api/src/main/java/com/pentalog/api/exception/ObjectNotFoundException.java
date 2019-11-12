package com.pentalog.api.exception;

import java.util.List;

import com.pentalog.api.dto.response.ApiError;

public class ObjectNotFoundException extends CustomExceptionContainingErrors {

	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(String message, ApiError error) {
		super(message, error);
	}

	public ObjectNotFoundException(String message, List<ApiError> errors) {
		super(message, errors);
	}

}
