package com.pgs.microservices.order.service;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.pgs.microservices.order.client.InventoryClient;
import com.pgs.microservices.order.dto.OrderRequest;
import com.pgs.microservices.order.event.OrderPlacedEvent;
import com.pgs.microservices.order.exception.ProductNotInStockException;
import com.pgs.microservices.order.model.Order;
import com.pgs.microservices.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class responsible for handling order placement operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;
	private final  KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

	/**
	 * Places an order if the requested product is available in stock.
	 *
	 * <p>This method first checks the inventory service to verify if the product
	 * identified by {@code skuCode} and requested quantity is in stock. If available,
	 * it maps the {@link OrderRequest} to an {@link Order} entity and saves it in the database.
	 * Otherwise, it throws a {@link RuntimeException} indicating the product is not available.</p>
	 *
	 * @param orderRequest the order request containing product SKU code and quantity
	 * @throws RuntimeException if the requested product is not in stock
	 */
	public void placeOrder(OrderRequest orderRequest) {
		var isProductInStock = this.inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
		if(isProductInStock) {
			log.debug("Received order request: {}", orderRequest);
	
			Order order = mapOrderRequestToOrder(orderRequest);
	
			log.debug("Mapped order: {}", order);
	
			orderRepository.save(order);
			
			// Send message to kafka topic
			sendOrderPlacedEvent(orderRequest, order);
		} else {
			log.debug("Product with skuCode {} is not in stock", orderRequest.skuCode());
			throw new ProductNotInStockException(orderRequest.skuCode());
		}
	}
	
	/**
	 * Sends an {@link OrderPlacedEvent} message to the Kafka topic {@code order-placed}.
	 * <p>
	 * This method creates an event containing the order number and the user's email
	 * from the provided {@link OrderRequest} and {@link Order}, then publishes it to
	 * the Kafka topic using the {@link KafkaTemplate}. 
	 * Logging is performed before and after the send operation for traceability.
	 * </p>
	 *
	 * @param orderRequest the incoming order request containing user details
	 * @param order the created order containing the generated order number
	 */
	private void sendOrderPlacedEvent(OrderRequest orderRequest, Order order) {
		OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(), orderRequest.userDetails().email());
		log.info("Start - Sending OrderPlaceEvent {} to kafka topic order-placed", orderPlacedEvent);
		kafkaTemplate.send("order-placed", orderPlacedEvent);
		log.info("End - Sending OrderPlaceEvent {} to kafka topic order-placed", orderPlacedEvent);	
		log.debug("Order successfully saved with order number: {}", order.getOrderNumber());
	}

	/**
	 * Maps an {@link OrderRequest} to an {@link Order} entity.
	 *
	 * @param orderRequest the order request to map
	 * @return the mapped Order entity
	 */
	private Order mapOrderRequestToOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setSkuCode(orderRequest.skuCode());
		order.setPrice(orderRequest.price());
		order.setQuantity(orderRequest.quantity());
		return order;
	}
}
