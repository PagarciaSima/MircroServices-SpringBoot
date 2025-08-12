package com.pgs.microservices.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import com.pgs.microservices.order.client.InventoryClient;
import com.pgs.microservices.order.dto.OrderRequest;
import com.pgs.microservices.order.exception.ProductNotInStockException;
import com.pgs.microservices.order.model.Order;
import com.pgs.microservices.order.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeOrder_WhenProductInStock_ShouldSaveOrder() {
        // Arrange: Mock inventory client to return true (product in stock)
        when(inventoryClient.isInStock("sku123", 2)).thenReturn(true);

        OrderRequest orderRequest = new OrderRequest(
            null, null, "sku123", BigDecimal.valueOf(50.0), 2
        );

        // Act
        orderService.placeOrder(orderRequest);

        // Assert: Verify orderRepository.save was called once with an Order having correct skuCode and quantity
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertEquals("sku123", savedOrder.getSkuCode());
        assertEquals(2, savedOrder.getQuantity());
        assertEquals(BigDecimal.valueOf(50.0), savedOrder.getPrice());
        assertNotNull(savedOrder.getOrderNumber()); // UUID generated
    }

    @Test
    void placeOrder_WhenProductNotInStock_ShouldThrowException() {
        // Arrange: Mock inventory client to return false (product NOT in stock)
        when(inventoryClient.isInStock("sku123", 2)).thenReturn(false);

        OrderRequest orderRequest = new OrderRequest(
            null, null, "sku123", BigDecimal.valueOf(50.0), 2
        );

        // Act & Assert: Expect ProductNotInStockException to be thrown
        ProductNotInStockException exception = assertThrows(ProductNotInStockException.class, () -> {
            orderService.placeOrder(orderRequest);
        });

        assertEquals("Product with skuCode sku123 is not in stock", exception.getMessage());

        // Also verify that orderRepository.save is never called
        verify(orderRepository, never()).save(any(Order.class));
    }
}
