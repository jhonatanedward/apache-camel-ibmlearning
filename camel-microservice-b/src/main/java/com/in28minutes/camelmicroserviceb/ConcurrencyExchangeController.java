package com.in28minutes.camelmicroserviceb;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcurrencyExchangeController {
	
	@GetMapping("/currencyExchange/from/{from}/to/{to}")
	public CurrencyExchange findConversionValue(@PathVariable String from, @PathVariable  String to) {
		return new CurrencyExchange(1000L, from, to, BigDecimal.TEN);
	}
	
}
