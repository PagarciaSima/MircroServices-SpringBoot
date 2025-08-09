package com.pgs.microservices.inventory.service;

import org.springframework.stereotype.Service;

import com.pgs.microservices.inventory.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

	private final InventoryRepository inventoryRepository ;
	
	/**
	 * Checks if a product with the given SKU code is currently in stock with at least the specified quantity.
	 *
	 * @param skuCode The unique identifier of the product.
	 * @param quantity The minimum desired quantity to check against.
	 * @return {@code true} if the product is in stock with the required quantity; {@code false} otherwise.
	 */
	public boolean isInStock(String skuCode, Integer quantity) {
        log.debug("Checking stock for SKU: {} with requested quantity: {}", skuCode, quantity);
		boolean inStock = this.inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
		
        if (inStock) {
            log.info("Product with SKU: {} is in stock with sufficient quantity.", skuCode);
        } else {
            log.warn("Product with SKU: {} is NOT in stock or does not have sufficient quantity.", skuCode);
        }
        return inStock;
	}
}
