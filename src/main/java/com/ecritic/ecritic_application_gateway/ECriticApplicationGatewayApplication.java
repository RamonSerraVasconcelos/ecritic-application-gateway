package com.ecritic.ecritic_application_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ECriticApplicationGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECriticApplicationGatewayApplication.class, args);
	}

	@Bean
	public WebProperties.Resources resources() {
		return new WebProperties.Resources();
	}

}
