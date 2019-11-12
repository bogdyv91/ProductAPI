package com.pentalog.api.exception;

import java.util.List;

import com.pentalog.api.response.ApiError;

public class ImpossibleToConvertPriceException extends CustomExceptionContainingErrors {

	private static final long serialVersionUID = 1L;

	public ImpossibleToConvertPriceException(String message) {
		super(message);
	}

	public ImpossibleToConvertPriceException(String message, ApiError error) {
		super(message, error);
	}

	public ImpossibleToConvertPriceException(String message, List<ApiError> errors) {
		super(message, errors);
	}
}
