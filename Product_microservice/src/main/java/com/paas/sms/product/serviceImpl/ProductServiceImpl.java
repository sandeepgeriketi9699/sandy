
package com.paas.sms.product.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paas.sms.product.model.Product;
import com.paas.sms.product.model.UserProductList;
import com.paas.sms.product.repository.ProductRepository;
import com.paas.sms.product.repository.UserProductRepository;
import com.paas.sms.product.service.ProductService;

@Service

@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserProductRepository userProductRepository;

	@Override
	public Iterable<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Product getProduct(long id) {
		return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public UserProductList save(UserProductList products) {
		UserProductList userProductList = userProductRepository.save(products);
		/*
		 * if (userProductList != null) { return "User Products Saved Successfully"; }
		 */
		return userProductList;

	}

	public void delete(UserProductList list) {
		userProductRepository.delete(list);
	}

	@Override
	public UserProductList findByEmail(String userEmailId) {

		return userProductRepository.findByEmail(userEmailId);
	}
}
