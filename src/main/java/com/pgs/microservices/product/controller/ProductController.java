package com.pgs.microservices.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pgs.microservices.product.dto.ProductRequest;
import com.pgs.microservices.product.dto.ProductResponse;
import com.pgs.microservices.product.model.Product;
import com.pgs.microservices.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "API for managing products")
public class ProductController {
	
	private final ProductService productService;
	
    /**
     * Creates a new product using the provided request body and returns the created product.
     * <p>
     * This endpoint handles HTTP POST requests and responds with status code 201 (Created) 
     * if the product is successfully persisted.
     * </p>
     *
     * @param productRequest the request body containing product name, description, and price
     * @return the created {@link Product} with a generated ID
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates a new product",
            description = "Creates a new product.",
            responses = {
                @ApiResponse(
                    responseCode = "201",
                    description = "Product successfully created",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProductResponse.class) 
                    )
                )
            }
        )
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return this.productService.createProduct(productRequest);
    }
    
    /**
     * Retrieves all products.
     * <p>
     * This endpoint handles HTTP GET requests and returns a list of all products
     * with status code 200 (OK).
     * </p>
     *
     * @return a list of {@link ProductResponse} representing all stored products
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all products",
            description = "Retrieves a list of all products.",
            responses = {
                @ApiResponse(
                    responseCode = "200",
                    description = "List of products retrieved successfully",
                    content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))
                    )
                )
            }
    )
    public List<ProductResponse> getAllProducts() {
        return this.productService.getAllProducts();
    }
}
