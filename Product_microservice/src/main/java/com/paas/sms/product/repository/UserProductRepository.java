package com.paas.sms.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.paas.sms.product.model.UserProductList;

public interface UserProductRepository extends MongoRepository<UserProductList, String>{
	
	public UserProductList findByEmail(String email);

}
