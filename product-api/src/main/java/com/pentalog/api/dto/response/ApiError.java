package com.pentalog.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
	private String details;
	private Object rejectedValue;
	private String rejectedField;
}
