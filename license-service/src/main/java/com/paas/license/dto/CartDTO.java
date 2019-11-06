package com.paas.license.dto;

import java.util.Set;

public class CartDTO {
	
	
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
