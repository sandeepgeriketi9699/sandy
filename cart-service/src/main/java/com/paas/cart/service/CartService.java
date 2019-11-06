package com.paas.cart.service;

import java.util.List;

import com.paas.cart.document.Cart;

public interface CartService {
	
	public Cart addToCart(Cart c);

	List<Cart> getCart();
	
	Cart findByEmail(String email);
	void deleteCart(String email);
	

}
