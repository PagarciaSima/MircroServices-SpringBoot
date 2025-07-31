package com.pgs.microservices.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgs.microservices.product.dto.ProductRequest;
import com.pgs.microservices.product.dto.ProductResponse;
import com.pgs.microservices.product.model.Product;
import com.pgs.microservices.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;

	/**
	 * Creates a new product based on the provided product request and saves it to the database.
	 * <p>
	 * Logs the creation process, including the product name, price, and the generated ID after saving.
	 * </p>
	 *
	 * @param productRequest the request containing the product's name, description, and price
	 * @return the saved {@link ProductResponse} entity with its generated ID
	 */
    public ProductResponse createProduct(ProductRequest productRequest) {
        log.info("Creating product: name='{}', price={}", productRequest.name(), productRequest.price());

        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        Product savedProduct = productRepository.save(product);

        log.info("Product created with ID: {}", savedProduct.getId());
        return this.mapProductToProductResponse(savedProduct);
    }

    /**
     * Retrieves all products from the database and maps them to {@link ProductResponse} DTOs.
     * <p>
     * This method returns a list of product responses that can be sent to API clients.
     * </p>
     *
     * @return a list of {@link ProductResponse} representing all stored products
     */
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products from the database");

        List<ProductResponse> products = productRepository.findAll()
                .stream()
                .map(this::mapProductToProductResponse)
                .toList();

        log.info("Found {} products", products.size());

        return products;
    }
	
	/**
	 * Maps a {@link Product} entity to a {@link ProductResponse} DTO.
	 * <p>
	 * This method is typically used to convert internal product data 
	 * into a response format suitable for API clients.
	 * </p>
	 *
	 * @param product the product entity to be mapped
	 * @return a {@link ProductResponse} containing the product's data
	 */
	private ProductResponse mapProductToProductResponse(Product product) {
	    log.debug("Mapping Product to ProductResponse: id={}, name={}", product.getId(), product.getName());

	    return new ProductResponse(
	            product.getId(),
	            product.getName(),
	            product.getDescription(),
	            product.getPrice()
	    );
	}

}
