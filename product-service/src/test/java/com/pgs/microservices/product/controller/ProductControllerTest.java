package com.pgs.microservices.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgs.microservices.product.dto.ProductRequest;
import com.pgs.microservices.product.dto.ProductResponse;
import com.pgs.microservices.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class) 
// Only loads the controller layer and simulates a web environment for HTTP requests
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; 
    // MockMvc allows performing HTTP requests to controller endpoints without running a server

    @Autowired
    private ObjectMapper objectMapper; 
    // Used for serializing Java objects into JSON strings and vice versa

    @MockBean
    private ProductService productService; 
    // Mocked ProductService is injected into the controller, isolating the controller tests

    private ProductResponse sampleProductResponse;

    @BeforeEach
    void setUp() {
        // Sample ProductResponse used in tests as a mocked return value
        sampleProductResponse = new ProductResponse(
                "1",
                "Product A",
                "sku123",
                "Desc A",
                BigDecimal.valueOf(10.0)
        );
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Arrange: mock the ProductService to return the predefined response on createProduct call
        Mockito.when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(sampleProductResponse);

        ProductRequest request = new ProductRequest(
                null, "Product A", "Desc A", BigDecimal.valueOf(10.0), "sku123"
        );

        // Act & Assert: perform HTTP POST request with JSON body, verify status and JSON response content
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Serialize request to JSON
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.id").value("1")) // Validate JSON response fields using JSONPath
                .andExpect(jsonPath("$.name").value("Product A"))
                .andExpect(jsonPath("$.skuCode").value("sku123"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {
        // Arrange: prepare a second product and mock getAllProducts to return a list of two products
        ProductResponse product2 = new ProductResponse(
                "2", "Product B", "sku456", "Desc B", BigDecimal.valueOf(15.0)
        );

        Mockito.when(productService.getAllProducts())
                .thenReturn(List.of(sampleProductResponse, product2));

        // Act & Assert: perform HTTP GET request, expect HTTP 200 OK and verify JSON array contents
        mockMvc.perform(get("/api/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.length()").value(2)) // Expect JSON array length to be 2
                .andExpect(jsonPath("$[0].name").value("Product A")) // Validate first item
                .andExpect(jsonPath("$[1].name").value("Product B")); // Validate second item
    }
}
