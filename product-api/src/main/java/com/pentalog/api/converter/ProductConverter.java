package com.pentalog.api.converter;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pentalog.api.dto.ProductDTO;
import com.pentalog.api.exception.InvalidRequestException;
import com.pentalog.api.model.Product;
import com.pentalog.api.response.ApiError;

@Component
public class ProductConverter {

	private CurrencyConverter currencyConverter;
	private @Value("${providedObjectNotValidMessage}") String providedObjectNotValidMessage;

	public ProductConverter(CurrencyConverter currencyConverter) {
		this.currencyConverter = currencyConverter;
	}

	public Product convertFromProductDTOtoProduct(ProductDTO productDTO) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
		if (violations.size() != 0) {
			List<ApiError> errors = new ArrayList<>();
			violations.forEach(v -> {
				errors.add(new ApiError(v.getMessage(), v.getInvalidValue(), v.getPropertyPath().toString()));
			});
			throw new InvalidRequestException(providedObjectNotValidMessage, errors);
		}
		Product product = Product.builder().name(productDTO.getName()).description(productDTO.getDescription())
				.price(currencyConverter.convertPriceToBase(productDTO.getPrice(), productDTO.getSymbol()).setScale(2,
						RoundingMode.FLOOR))
				.available(productDTO.isAvailable()).stock(productDTO.getStock()).image(productDTO.getImage()).build();
		return product;
	}
}