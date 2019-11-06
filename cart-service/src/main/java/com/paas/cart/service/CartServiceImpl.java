package com.paas.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paas.cart.document.Cart;
import com.paas.cart.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

	
	  @Autowired CartRepository cartRepository;
	 

	@Override
	public Cart addToCart(Cart o) {
		//cartRepository.save(o);
		return cartRepository.save(o);
	
	}
	
	@Override
	public List<Cart> getCart() {
		//cartRepository.save(o);
		return cartRepository.findAll();
	}

	@Override
	public Cart findByEmail(String email) {
		// TODO Auto-generated method stub
		return cartRepository.findByEmail(email) ;
	}

	@Override
	public void deleteCart(String email) {
	Cart cart=	cartRepository.findByEmail(email) ;
		cartRepository.delete(cart);
		
	}
	

	/*
	 * @Override public Cart getCart() { //cartRepository.save(o); return
	 * cartRepository.findByEmail(); }
	 */

}
