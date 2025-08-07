package com.pgs.microservices.order.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

/**
 * This interface uses Spring's modern declarative HTTP client with @GetExchange,
 * introduced in Spring Framework 6 / Spring Boot 3.
 * 
 * Note: OpenFeign, while still supported, is no longer the recommended approach for new projects.
 * The new Spring WebClient-based declarative clients provide a lighter, more flexible, and
 * native Spring alternative.
 */
public interface InventoryClient {

	@GetExchange("/api/inventory")
	boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
}
