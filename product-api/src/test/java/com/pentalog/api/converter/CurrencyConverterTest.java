package com.pentalog.api.converter;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.pentalog.api.exception.ImpossibleToConvertPriceException;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConverterTest {

	@Mock
	RestTemplate restTemplate;

	@InjectMocks
	CurrencyConverterImpl currencyConverter;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(currencyConverter);
	}

	@Test(expected = ImpossibleToConvertPriceException.class)
	public void testConvertPriceShouldThrowRestClientException() {
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.eq(String.class)))
				.thenThrow(RestClientException.class);
		currencyConverter.convertPriceToBase(new BigDecimal(123), "RON");
	}

	@Test(expected = ImpossibleToConvertPriceException.class)
	public void testConvertPriceShouldThrowJsonException() {
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.eq(String.class)))
				.thenReturn(new ResponseEntity<String>("ceva", HttpStatus.OK));
		currencyConverter.convertPriceToBase(new BigDecimal(123), "RON");
	}

	@Test
	public void testConvertPriceShouldWork() {
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.eq(String.class)))
				.thenReturn(new ResponseEntity<String>(
						"{\"rates\":{\"RON\":4.7577},\"base\":\"EUR\",\"date\":\"2019-10-30\"}", HttpStatus.OK));
		currencyConverter.convertPriceToBase(new BigDecimal(123), "RON");
	}
}
