package com.pgs.microservices.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pgs.microservices.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
