package com.paas.sms.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.paas.sms.product.model.Product;



public interface ProductRepository extends MongoRepository<Product, Long> {

	public List<Product> findByProductNameLike(String productName);

	public Product save(Optional<Product> p);
}
