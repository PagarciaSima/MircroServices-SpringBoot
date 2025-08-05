package com.pgs.microservices.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pgs.microservices.inventory.service.InventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "API for managing Inventory")
public class InventoryController {

	private final InventoryService inventoryService ;
	
	/**
	 * Checks if a product with the given SKU code has at least the specified quantity in stock.
	 *
	 * @param skuCode  the SKU code of the product to check availability for
	 * @param quantity the quantity to check for stock availability
	 * @return {@code true} if the product is in stock with the requested quantity, {@code false} otherwise
	 */
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(
            summary = "Checks if a product is in stock",
            description = "Creates a new order.",
            responses = {
                @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Boolean.class) 
                    )
                )
            }
        )
	public boolean isInStock(
			@RequestParam @NotBlank String skuCode, 
			@RequestParam (name = "quantity")  @NotNull @Min(1) Integer quantity) {
		return this.inventoryService.isInStock(skuCode, quantity);
	}
}
