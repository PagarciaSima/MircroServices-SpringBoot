package com.pgs.microservices.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pgs.microservices.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks and inject them
    }

    @Test
    void isInStock_ShouldReturnTrue_WhenRepositoryReturnsTrue() {
        // Arrange: mock repository to return true
        String skuCode = "sku123";
        int quantity = 10;
        when(inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity)).thenReturn(true);

        // Act
        boolean result = inventoryService.isInStock(skuCode, quantity);

        // Assert
        assertTrue(result);
        verify(inventoryRepository, times(1)).existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

    @Test
    void isInStock_ShouldReturnFalse_WhenRepositoryReturnsFalse() {
        // Arrange: mock repository to return false
        String skuCode = "sku123";
        int quantity = 10;
        when(inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity)).thenReturn(false);

        // Act
        boolean result = inventoryService.isInStock(skuCode, quantity);

        // Assert
        assertFalse(result);
        verify(inventoryRepository, times(1)).existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }
}
