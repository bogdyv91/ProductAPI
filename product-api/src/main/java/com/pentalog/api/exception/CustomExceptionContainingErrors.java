package com.pentalog.api.exception;

import java.util.ArrayList;
import java.util.List;

import com.pentalog.api.dto.response.ApiError;

public class CustomExceptionContainingErrors extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<ApiError> errors;

	public CustomExceptionContainingErrors(String message) {
		super(message);
	}

	public CustomExceptionContainingErrors(String message, ApiError error) {
		super(message);
		errors = new ArrayList<>();
		errors.add(error);
	}

	public CustomExceptionContainingErrors(String message, List<ApiError> errors) {
		super(message);
		this.errors = errors;
	}

	public List<ApiError> getErrors() {
		return errors;
	}

	public void setErrors(List<ApiError> errors) {
		this.errors = errors;
	}
}
