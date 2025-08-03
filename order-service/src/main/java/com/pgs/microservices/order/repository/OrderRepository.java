package com.pgs.microservices.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgs.microservices.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
