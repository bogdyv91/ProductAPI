package com.pentalog.api.converter;

import java.math.BigDecimal;

public interface CurrencyConverter {

	public BigDecimal convertPriceToBase(BigDecimal price, String symbol);
}
