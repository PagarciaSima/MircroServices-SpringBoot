package com.pgs.microservices.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgs.microservices.order.dto.OrderRequest;
import com.pgs.microservices.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class) // Load only the OrderController and related MVC components
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc simulates HTTP requests and allows assertions on responses

    @Autowired
    private ObjectMapper objectMapper;  // Used to serialize Java objects to JSON strings

    @MockBean
    private OrderService orderService;  // Mock the OrderService dependency to isolate controller tests

    private OrderRequest validOrderRequest;

    @BeforeEach
    void setUp() {
        // Prepare a valid OrderRequest object used in tests
        validOrderRequest = new OrderRequest(
            null,             // id is null because it's not required when creating a new order
            null,             // orderNumber is generated internally, so also null here
            "sku123",         // Sample SKU code
            BigDecimal.valueOf(99.99), // Sample price
            5                 // Sample quantity
        );
    }

    @Test
    void placeOrder_ShouldReturnSuccessMessage_WhenOrderIsValid() throws Exception {
        // Arrange: Mock the OrderService to do nothing when placeOrder is called (void method)
        Mockito.doNothing().when(orderService).placeOrder(any(OrderRequest.class));

        // Act & Assert: Perform a POST request to /api/order with the valid request serialized as JSON
        // Expect HTTP status 201 Created and response body contains the success message
        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order Placed successfully"));
    }

    @Test
    void placeOrder_ShouldReturnBadRequest_WhenInvalidOrderRequest() throws Exception {
        // Arrange: Create an invalid OrderRequest where quantity is 0 (violates validation constraint @Min(1))
        OrderRequest invalidRequest = new OrderRequest(
            null, null, "sku123", BigDecimal.valueOf(50), 0
        );

        // Act & Assert: Perform a POST request with the invalid request and expect HTTP 400 Bad Request
        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
