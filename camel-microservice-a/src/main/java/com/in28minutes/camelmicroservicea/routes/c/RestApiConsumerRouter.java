package com.in28minutes.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RestApiConsumerRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		restConfiguration().host("localhost").port(8000);
		
		from("timer:rest-api-consumer?period=5000")
		.setHeader("from", () ->"EUR")
		.setHeader("to", () -> "INR")
		.log("${body}")
		.to("rest:get:currencyExchange/from/{from}/to/{to}")
		.log("${body}");
	}
}
