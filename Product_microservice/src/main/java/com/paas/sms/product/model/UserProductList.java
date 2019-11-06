package com.paas.sms.product.model;

import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

public class UserProductList {

	@Id
	private String id;

	@Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
	private String email;
	private List<Product> products;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
