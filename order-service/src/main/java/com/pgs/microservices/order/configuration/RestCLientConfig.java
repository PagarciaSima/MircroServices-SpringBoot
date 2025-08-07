package com.pgs.microservices.order.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.pgs.microservices.order.client.InventoryClient;

@Configuration
public class RestCLientConfig {
	
	@Value("${inventory.url}")
	private String inventoryServiceUrl;

	/**
	 * Creates a declarative HTTP client proxy for the Inventory service using Spring's RestClient.
	 * 
	 * This method builds a RestClient configured with the base URL of the inventory service,
	 * adapts it to Spring's HTTP service proxy infrastructure, and then creates a proxy instance
	 * of the InventoryClient interface.
	 * 
	 * When calling methods on the returned InventoryClient, the HTTP requests will be sent to
	 * the configured base URL plus the path defined in the interface's annotations (e.g., @GetExchange).
	 * 
	 * @return an implementation of InventoryClient that communicates with the inventory microservice
	 */
	@Bean
	public InventoryClient inventoryClient() {
	    // Build the RestClient with the base URL of the inventory service
	    RestClient restClient = RestClient.builder()
	            .baseUrl(inventoryServiceUrl)  // e.g. "http://localhost:8082"
	            .build();

	    // Adapt the RestClient so it can be used by Spring's HTTP service proxy factory
	    var restClientAdapter = RestClientAdapter.create(restClient);

	    // Create the proxy factory using the adapted RestClient
	    var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

	    // Create and return the client proxy that implements InventoryClient interface
	    return httpServiceProxyFactory.createClient(InventoryClient.class);
	}
}
