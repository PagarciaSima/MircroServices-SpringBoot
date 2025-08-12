package com.pgs.microservices.product.service;

import com.pgs.microservices.product.dto.ProductRequest;
import com.pgs.microservices.product.dto.ProductResponse;
import com.pgs.microservices.product.model.Product;
import com.pgs.microservices.product.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    // Mocked repository to avoid touching the real database
    @Mock
    private ProductRepository productRepository;

    // Service under test, with mocked dependencies injected
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations (mocks and inject mocks)
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ShouldSaveProductAndReturnResponse() {
        // Arrange: prepare the input request
        ProductRequest request = new ProductRequest(
                null, // id is null because it will be generated upon saving
                "Product A",
                "Desc A",
                BigDecimal.valueOf(10.0),
                "sku123"
        );

        // Mocked saved product returned by the repository
        Product savedProduct = Product.builder()
                .id("1")
                .name("Product A")
                .description("Desc A")
                .price(BigDecimal.valueOf(10.0))
                .skuCode("sku123")
                .build();

        // Define repository behavior when save() is called
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act: call the service method
        ProductResponse response = productService.createProduct(request);

        // Assert: verify that the response contains the expected data
        assertThat(response.id()).isEqualTo("1");
        assertThat(response.name()).isEqualTo("Product A");
        assertThat(response.skuCode()).isEqualTo("sku123");
        assertThat(response.price()).isEqualByComparingTo("10.0");

        // Capture the product passed to the repository and verify its fields
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Product A");
        assertThat(captor.getValue().getSkuCode()).isEqualTo("sku123");
    }

    @Test
    void getAllProducts_ShouldReturnMappedResponses() {
        // Arrange: prepare a list of products to be returned by the repository
        Product product1 = Product.builder()
                .id("1")
                .skuCode("sku1")
                .name("Prod 1")
                .description("Desc 1")
                .price(BigDecimal.valueOf(5))
                .build();

        Product product2 = Product.builder()
                .id("2")
                .skuCode("sku2")
                .name("Prod 2")
                .description("Desc 2")
                .price(BigDecimal.valueOf(15))
                .build();

        // Mock repository to return the prepared list
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // Act: call the service method
        List<ProductResponse> responses = productService.getAllProducts();

        // Assert: verify list size and content mapping
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo("Prod 1");
        assertThat(responses.get(1).name()).isEqualTo("Prod 2");

        // Verify that findAll() was called exactly once
        verify(productRepository, times(1)).findAll();
    }
}
