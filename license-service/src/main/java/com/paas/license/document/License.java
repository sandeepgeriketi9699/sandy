package com.paas.license.document;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.paas.license.dto.ProductDTO;

@Document
public class License {

	private Set<ProductDTO> uniqueProducts;

	@Id
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "License [products=" + ", email=" + email + "]";
	}

	public Set<ProductDTO> getUniqueProducts() {
		return uniqueProducts;
	}

	public void setUniqueProducts(Set<ProductDTO> uniqueProducts) {
		this.uniqueProducts = uniqueProducts;
	}

}
