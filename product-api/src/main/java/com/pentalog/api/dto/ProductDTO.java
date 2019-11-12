package com.pentalog.api.dto;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
@Valid
public class ProductDTO {

	@NotEmpty
	private String name;
	private String description;
	private String symbol;
	@NotNull @Positive
	private BigDecimal price;
	private boolean available;
	private int stock;
	private byte[] image;
}
