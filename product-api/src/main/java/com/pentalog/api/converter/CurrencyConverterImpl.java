package com.pentalog.api.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.pentalog.api.dto.response.ApiError;
import com.pentalog.api.exception.ImpossibleToConvertPriceException;

@Service
public class CurrencyConverterImpl implements CurrencyConverter {

	private RestTemplate restTemplate;
	private String baseSymbol;
	private String exchangeUrl;
	private String errorMessage;
	private @Value("${symbolNotAvailableErrorMessage}") String symbolNotAvailableErrorMessage;
	private @Value("${exchangeApiNotAvailableErrorMessage}") String exchangeApiNotAvailableErrorMessage;

	@Autowired
	public CurrencyConverterImpl(RestTemplate restTemplate, @Value("${baseSymbol}") String baseSymbol,
			@Value("${exchangeURL}") String exchangeUrl, @Value("${exchangeErrorMessage}") String errorMessage) {
		this.restTemplate = restTemplate;
		this.baseSymbol = baseSymbol;
		this.errorMessage = errorMessage;
		this.exchangeUrl = exchangeUrl;
	}

	private BigDecimal getRateForSymbol(String symbol) {
		try {
			ResponseEntity<String> response = restTemplate.exchange(
					exchangeUrl + "base=" + baseSymbol + "&symbols=" + symbol, HttpMethod.GET, null, String.class);
			JSONObject responseObject = new JSONObject(response.getBody());
			BigDecimal rate = new BigDecimal(responseObject.getJSONObject("rates").getDouble(symbol));
			return rate;
		} catch (JSONException ex) {
			throw new ImpossibleToConvertPriceException(errorMessage,
					new ApiError(symbolNotAvailableErrorMessage, symbol, "symbol"));
		} catch (RestClientException ex) {
			throw new ImpossibleToConvertPriceException(errorMessage,
					new ApiError(exchangeApiNotAvailableErrorMessage, "", ""));
		}
	}

	public BigDecimal convertPriceToBase(BigDecimal price, String symbol) {
		if (symbol.equals(baseSymbol)) {
			return price;
		}
		BigDecimal rate = this.getRateForSymbol(symbol);
		return price.divide(rate, 2, RoundingMode.FLOOR);
	}
}
