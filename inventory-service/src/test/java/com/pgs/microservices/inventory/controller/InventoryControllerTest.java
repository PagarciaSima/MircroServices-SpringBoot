package com.pgs.microservices.inventory.controller;

import com.pgs.microservices.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class) 
// Load only the web layer (InventoryController) for testing, without starting the full Spring context
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;  
    // MockMvc is used to simulate HTTP requests to the controller

    @MockBean
    private InventoryService inventoryService; 
    // Mock the InventoryService dependency, so the controller can be tested in isolation

    @BeforeEach
    void setUp() {
    }

    @Test
    void isInStock_ShouldReturnTrue_WhenProductIsInStock() throws Exception {
        // Arrange: mock service to return true for any skuCode and quantity
        Mockito.when(inventoryService.isInStock(anyString(), anyInt())).thenReturn(true);

        // Act & Assert: perform GET request to /api/inventory with parameters
        // Expect HTTP status 200 (OK) and response body "true"
        mockMvc.perform(get("/api/inventory")
                        .param("skuCode", "sku123")
                        .param("quantity", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void isInStock_ShouldReturnFalse_WhenProductIsNotInStock() throws Exception {
        // Arrange: mock service to return false to simulate out of stock scenario
        Mockito.when(inventoryService.isInStock(anyString(), anyInt())).thenReturn(false);

        // Act & Assert: perform GET request and expect HTTP 200 with body "false"
        mockMvc.perform(get("/api/inventory")
                        .param("skuCode", "sku123")
                        .param("quantity", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void isInStock_ShouldReturnBadRequest_WhenQuantityIsInvalid() throws Exception {
        // Act & Assert: send quantity=0 which violates @Min(1) validation on controller
        // Expect HTTP 400 Bad Request due to validation failure
        mockMvc.perform(get("/api/inventory")
                        .param("skuCode", "sku123")
                        .param("quantity", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void isInStock_ShouldReturnBadRequest_WhenSkuCodeIsMissing() throws Exception {
        // Act & Assert: missing required skuCode parameter, expect HTTP 400 Bad Request
        mockMvc.perform(get("/api/inventory")
                        .param("quantity", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
