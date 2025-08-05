package com.pgs.microservices.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
	    String id,

	    @NotBlank(message = "Name is required")
	    String name,

	    @Size(max = 500, message = "Description must be at most 500 characters")
	    String description,

	    @NotNull(message = "Price is required")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
	    BigDecimal price
	) {

	}