package com.paas.sms.product.service;

import com.paas.sms.product.model.Product;
import com.paas.sms.product.model.UserProductList;

public interface ProductService {
	
	public Iterable<Product> getAllProducts();

	public Product getProduct(long id);

	public Product save(Product product);

	public UserProductList save(UserProductList products);

	public UserProductList findByEmail(String userEmailId);
	
	public void delete(UserProductList list);

}
