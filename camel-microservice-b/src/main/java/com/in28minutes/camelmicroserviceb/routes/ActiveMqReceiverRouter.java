package com.in28minutes.camelmicroserviceb.routes;

import java.math.BigDecimal;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.in28minutes.camelmicroserviceb.CurrencyExchange;

@Component
public class ActiveMqReceiverRouter extends RouteBuilder{
	
	

	@Autowired
	MyCurrencyExchangeProcessor myCurrencyExchangeProcessor;
	
	@Autowired
	MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;
	
	@Override
	public void configure() throws Exception {
		
//		from("activemq:my-activemq-queue")
//		.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
//		.bean(myCurrencyExchangeProcessor)
//		.bean(myCurrencyExchangeTransformer)
//		.to("log:received-message-from-active-mq");
		
		from("activemq:my-activemq-xmlqueue")
		.unmarshal()
		.jacksonxml(CurrencyExchange.class)
		.to("log:received-message-from-active-mq");
	}
	
}


@Component
class MyCurrencyExchangeProcessor {
	
	Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);
	
	public void processMessage(CurrencyExchange currencyExchange) {
		logger.info("Do something with currencyExchange.getConversionMultiple.., which value is: {}" + currencyExchange.getConversionMultiple()); 	
	}
	
}

@Component
class MyCurrencyExchangeTransformer {
	
	Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeTransformer.class);
	
	public CurrencyExchange processMessage(CurrencyExchange currencyExchange) {
		
		currencyExchange.setConversionMultiple(currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));
		
		logger.info("Multimply currencyExchange value, which value is {}", currencyExchange.getConversionMultiple()); 	
		
		return currencyExchange;
	}
	
}