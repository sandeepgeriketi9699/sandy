package com.paas.cart.document;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.paas.cart.dto.ProductDTO;

@Document
public class Cart {

	@Id
	private String email;

	private Set<ProductDTO> uniqueProductDTO;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Cart [products=" + ", email=" + email + "]";
	}

	public Set<ProductDTO> getUniqueProductDTO() {
		return uniqueProductDTO;
	}

	public void setUniqueProductDTO(Set<ProductDTO> uniqueProductDTO) {
		this.uniqueProductDTO = uniqueProductDTO;
	}

}
