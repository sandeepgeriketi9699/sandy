package com.paas.cart.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.paas.cart.document.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {
	
	Cart findByEmail(String email);

}
