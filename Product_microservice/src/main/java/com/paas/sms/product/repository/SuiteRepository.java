package com.paas.sms.product.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.paas.sms.product.model.Product;
import com.paas.sms.product.model.Suite;


public interface SuiteRepository extends MongoRepository<Suite,  Long>  {

	public List<Suite> findBySuiteNameLike(String suietName);

	//public List<Product> saveAll(Product[] products);

	public void save(Product product);

	public Suite findBySuiteName(String suiteName);
}
