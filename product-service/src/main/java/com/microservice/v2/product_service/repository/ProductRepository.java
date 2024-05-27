package com.microservice.v2.product_service.repository;

import com.microservice.v2.product_service.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
