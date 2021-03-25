package com.in28minutes.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqSenderRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		// timer
//		from("timer:active-mq?period=10000")
//		.transform().constant("My message for active MQ")
//		.log("${body}")
//		.to("activemq:my-activemq-queue");
		//queue
		
//		from("file:files/json")
//		.log("${body}")
//		.to("activemq:my-activemq-queue");
		
		from("file:files/xml")
		.log("${body}")
		.to("activemq:my-activemq-xmlqueue");
	}

}
