package com.in28minutes.camelmicroservicea.routes.pattenrs;

import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.in28minutes.camelmicroservicea.CurrencyExchange;

// @Component
public class EipPatternsRouter extends RouteBuilder{

	

	@Autowired
	SplitterComponent splitterComponent;
	
	@Autowired
	DynamicRouterBean dynamicRouterBean;
	
	@Override
	public void configure() throws Exception {
		
		errorHandler(deadLetterChannel("activemq:dead-letter-queue"));
		
//		from("timer:multicast-timer?period=10000")
//		.multicast()
//		.to("log:somenthing1", "log:something2");
				
//		from("file:files/csv")
//		.unmarshal().csv()
//		.split(body())
//		.to("activemq:split-queue");
		
		//Message,Message2,Message3
//		from("file:files/csv")
//		.convertBodyTo(String.class)
//		.split(method(splitterComponent))
//		.to("activemq:split-queue");
//		
		// Aggregate
		// Messages => Aggregate => Endpoint
		// to, 3
		
		from("file:files/aggregate-json")
		.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
		.aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
		.completionSize(3)
		//.completionTimeout(HIGEST);
		.to("log:aggregate-json");
		
		String routingSplip = "direct:endpoint1,direct:endpoint2";
		// String routingSplip = "direct:enpoint,direct:endpoint2,direct:endpint:3";
		
		
//		from("timer:router-slip?period=10000")
//		.transform().constant("My message is hardcoded")
//		.routingSlip(simple(routingSplip));
		
		getContext().setTracing(true);
		
		
		
		from("timer:router-slip?period={{timePeriod}}")
		.transform().constant("My message is hardcoded")
		.dynamicRouter(method(dynamicRouterBean));

		
		from("direct:endpoint1")
		.wireTap("log:write-tap")
		.to("{{endpoint-for-loggin}}");
		
		from("direct:endpoint2")
		.to("log:directendpoint2");
		
		from("direct:endpoint3")
		.to("log:directendpoint3");
		
		
	}
	
}

@Component
class SplitterComponent{
	public List<String> splitInput(String body){
		return List.of(body.split(","));
	}
}

@Component
class DynamicRouterBean {
	Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);
	
	int invocations;
	
	public String decideTheNextEndpoint(
			@ExchangeProperties Map<String, String> properties,
			@Headers Map<String, String> headers,
			@Body String body) {
		logger.info("{}, {}, {}", properties, headers, body);
		
		invocations++;
		
		if(invocations % 3 == 0) {
			return "direct:endpoint1";
		}
		if(invocations % 3 ==1) {
			return "direct:endpoint2,direct:endpoint3";
		}
		
		
		return null;
		
	}
}







