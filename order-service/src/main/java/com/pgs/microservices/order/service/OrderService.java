package com.pgs.microservices.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pgs.microservices.order.dto.OrderRequest;
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

	/**
	 * Places a new order by mapping the order request and saving it to the repository.
	 *
	 * @param orderRequest the incoming order request containing product and quantity details
	 */
	public void placeOrder(OrderRequest orderRequest) {
		log.debug("Received order request: {}", orderRequest);

		Order order = mapOrderRequestToOrder(orderRequest);

		log.debug("Mapped order: {}", order);

		orderRepository.save(order);

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
