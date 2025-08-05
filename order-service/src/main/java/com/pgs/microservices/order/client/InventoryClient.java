package com.pgs.microservices.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pgs.microservices.order.constants.InventoryConstants;

@FeignClient(value = InventoryConstants.FEIGN_CLIENT_NAME, url = InventoryConstants.INVENTORY_SERVICE_BASE_URL)
public interface InventoryClient {

	@GetMapping(value = "/api/inventory") 
	boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
}
