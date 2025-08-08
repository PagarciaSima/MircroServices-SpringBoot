package com.pgs.microservices.order.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
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
	            .requestFactory(getClientRequestFactory()) // Config for http request timeout
	            .build();

	    // Adapt the RestClient so it can be used by Spring's HTTP service proxy factory
	    var restClientAdapter = RestClientAdapter.create(restClient);

	    // Create the proxy factory using the adapted RestClient
	    var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

	    // Create and return the client proxy that implements InventoryClient interface
	    return httpServiceProxyFactory.createClient(InventoryClient.class);
	}

	/**
	 * Creates and configures a ClientHttpRequestFactory with custom timeout settings.
	 * 
	 * This factory is used to create HTTP requests with specific connection and read timeouts.
	 * It ensures that the client will wait up to 3 seconds to establish a connection 
	 * and up to 3 seconds to read data from the server before timing out.
	 *
	 * @return a configured ClientHttpRequestFactory instance with timeouts set
	 */
	private ClientHttpRequestFactory getClientRequestFactory() {
	    // Start with the default settings for the ClientHttpRequestFactory
	    ClientHttpRequestFactorySettings clientHttpRequestFactorySettings = ClientHttpRequestFactorySettings.DEFAULTS
	            // Set the maximum time to establish a connection to 3 seconds
	            .withConnectTimeout(Duration.ofSeconds(3))
	            // Set the maximum time to wait for reading data to 3 seconds
	            .withReadTimeout(Duration.ofSeconds(3));
	    
	    // Create and return a ClientHttpRequestFactory instance using the above settings
	    return ClientHttpRequestFactories.get(clientHttpRequestFactorySettings);
	}
}
