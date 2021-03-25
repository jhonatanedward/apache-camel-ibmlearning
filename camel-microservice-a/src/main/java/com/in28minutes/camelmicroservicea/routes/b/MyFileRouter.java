package com.in28minutes.camelmicroservicea.routes.b;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyFileRouter extends RouteBuilder{
	
	@Autowired
	private DeciderBean deciderBean;
	
	@Override
	public void configure() throws Exception {
		from("file:files/input")
		.routeId("Files-Input-Routes")
		.transform().body(String.class)
		.choice()
			.when(simple("${file:ext} == 'xml'"))
				.log("XML File")
			//.when(simple("${body} contains 'USD'"))
			.when(method(deciderBean))
				.log("Not an xml file but contains USD")
			.otherwise()
				.log("Not an XML File")
		.end()
		//.to("direct:log-files-values")
		.to("file:files/output");
		
		
		from("direct:log-files-values")
		.log("${body}")
		.log("${messageHistory} ${headers.CamelFileAbsolute}")
		.log("${messageHistory} ${file:absolute.path}")
	   	.log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
	   	.log("${file:onlyname.noext} ${file:parent} ${file:path} ${file:absolute}")
	   	.log("${file:size} ${file:modified}")
	   	.log("${routeId} ${camelId} ${body}");
	}

}


@Component
class DeciderBean{
	
	Logger logger = LoggerFactory.getLogger(DeciderBean.class);
	
	public boolean isThisConditionMet(@Body String body,
			@Headers Map<String, String> headers,
			@ExchangeProperties Map<String, String> exchangeProperties
			) {
		logger.info("Decide Bean {} {} {}", body, headers, exchangeProperties);
		
		return true;
	}
}