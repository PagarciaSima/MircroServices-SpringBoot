package com.pgs.microservices.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

/**
 * This interface uses Spring's modern declarative HTTP client with @GetExchange,
 * introduced in Spring Framework 6 / Spring Boot 3.
 * 
 * Note: OpenFeign, while still supported, is no longer the recommended approach for new projects.
 * The new Spring WebClient-based declarative clients provide a lighter, more flexible, and
 * native Spring alternative.
 */
public interface InventoryClient {
	
	final static Logger LOGGER = LoggerFactory.getLogger(InventoryClient.class);

	@GetExchange("/api/inventory")
	// This name should match the app properties for resilience setup
	@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
	// Retry if request fails
	@Retry(name = "inventory")
	boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
	
	default boolean fallbackMethod(String code, Integer quantity, Throwable throwable) {
		LOGGER.info("Cannot get inventory for skucode {}, failure reason {}", code, throwable.getMessage());
		return false;
	}
}
