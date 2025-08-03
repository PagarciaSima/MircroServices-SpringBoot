package com.pgs.microservices.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pgs.microservices.order.dto.OrderRequest;
import com.pgs.microservices.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Tag(name = "Order", description = "API for managing Orders")
public class OrderController {

	private static final String ORDER_PLACED_SUCCESSFULLY_MSG = "Order Placed successfully";
	private final OrderService orderService;
	
	
    /**
     * Handles the HTTP POST request to create a new order.
     *
     * @param orderRequest the order request payload containing SKU code, price, and quantity
     * @return a confirmation message indicating the order was successfully placed
     *
     * @see OrderRequest
     */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
            summary = "Creates a new order",
            description = "Creates a new order.",
            responses = {
                @ApiResponse(
                    responseCode = "201",
                    description = "Order successfully created",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = String.class) 
                    )
                )
            }
        )
	public String placeOrder(@RequestBody OrderRequest orderRequest) {
		this.orderService.placeOrder(orderRequest);
		return ORDER_PLACED_SUCCESSFULLY_MSG;
	}
}
