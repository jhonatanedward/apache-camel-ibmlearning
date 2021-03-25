package com.in28minutes.camelmicroservicea.routes.a;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// @Component
public class MyFirstTimerRouter extends RouteBuilder{
	
	

	@Autowired
	private GetCurrentTimeBean getCurrentTimeBean;
	
	@Autowired
	private SimpleLoggingProcessComponent loggingComponent;
	
	@Override
	public void configure() throws Exception {
		// queue
		from("timer:first-timer")
		.log("${body}")
		.transform().constant("My Constant Message")
		.log("${body}")
		//.transform().constant("My Constant Message" + LocalDateTime.now())
		//.bean("getCurrentTimeBean")
		.bean(getCurrentTimeBean)
		.log("${body}")
		.bean(loggingComponent)
		.process(new SimpleLoggingProcessor())
		.to("log:frist-timer");
		
		// transformation
		
		// log
	}
	
}

class SimpleLoggingProcessor implements Processor {

	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessComponent.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		logger.info("SimpleLoggingProcessComponent {}", exchange.getMessage().getBody());
	}

}


@Component
class GetCurrentTimeBean{
	public String getCurrentTime() {
		return "Time now is + " + LocalDateTime.now();
	}
}

@Component
class SimpleLoggingProcessComponent{
	
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessComponent.class);
	
	public void process(String message) {
		logger.info("SimpleLoggingProcessComponent {}", message);
	}
}