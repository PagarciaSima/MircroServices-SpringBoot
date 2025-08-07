package com.pgs.microservices.product.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

	@OpenAPIDefinition(
		info = @Info (
			contact = @Contact (
				name = "Pablo",
				email = "pablo.garciasimavilla@gmail.com",
				url = "https://www.linkedin.com/in/pablo-garc%C3%ADa-simavilla-756469222/"
			),
			description = "OpenApi Micro Services - Products",
			title = "OpenApi specification - Pablo García Simavilla",
			version = "1.0"
		),
		servers  = {
			@Server(
				description = "Local environment",
				url = "http://localhost:8080"
			)
		}
	)
	// http://localhost:8080/swagger-ui/index.html
	public class OpenApiConfiguration {

	}
