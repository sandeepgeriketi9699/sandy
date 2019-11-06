package com.paas.cart.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.paas.cart.document.Cart;
import com.paas.cart.dto.AmountDTO;
import com.paas.cart.dto.CouponDTO;
import com.paas.cart.dto.ProductDTO;
import com.paas.cart.repository.CartRepository;
import com.paas.cart.service.CartService;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController {

	@Autowired
	public CartService cartService;

	@GetMapping("/{email}")
	public Cart getCartById(@PathVariable String email) throws Exception {
		Cart cart = cartService.findByEmail(email);
		if (cart != null) {
			return cart;
		}
		throw new Exception("Cart not found ");
	}

	@DeleteMapping("/{email}")
	public String deleteCart(@PathVariable String email) {
		cartService.deleteCart(email);
		return "Cart deleted";
	}

	@DeleteMapping("/product/{emailId}/{productId}")
	public String deleteProductFromCart(@PathVariable long productId, @PathVariable String emailId) throws Exception {
		Cart cart = cartService.findByEmail(emailId);
		Set<ProductDTO> products = (cart != null) ? cart.getUniqueProductDTO() : null;
		if (!CollectionUtils.isEmpty(products)) {
			Set<ProductDTO> updatedProducts = products.stream().filter(prd -> !(productId == prd.getProductId()))
					.collect(Collectors.toSet());

			cart.setUniqueProductDTO(updatedProducts);
			cart.setEmail(emailId);
			cartService.addToCart(cart);
			return "Product removed from cart";
		} else {
			throw new Exception("No products available in cart");
		}

	}

	@PutMapping("/update/{newMail}/{oldMail}")
	public void update(@PathVariable String newMail, @PathVariable String oldMail) {
		System.out.println("******* Entered into updateEmail in Cart Controller ************");
		Cart cart = cartService.findByEmail(oldMail);
		System.out.println("********************* Cart ****************** ::" + cart);
		if (cart != null) {

			cart.setEmail(newMail);
			cart.setUniqueProductDTO(cart.getUniqueProductDTO());
			cartService.addToCart(cart);
			Cart oldCart = cartService.findByEmail(oldMail);
			if (oldCart != null) {
				cartService.deleteCart(oldMail);
			}
		}
	}

	@PostMapping("/{emailId}")
	public Cart addToCart(@RequestBody Set<ProductDTO> products, @PathVariable String emailId) throws Exception {

		List<Long> prdIds = products.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
		/*
		 * for (ProductDTO p : products) { prdIds.add(p.getProductId()); }
		 */
		Cart cart = cartService.findByEmail(emailId);

		if (cart != null) {
			System.out.println("Eneterd into if block");

			Set<ProductDTO> dbProducts = cart.getUniqueProductDTO();
			if (dbProducts != null) {
				for (ProductDTO prd : dbProducts) {
					if (prdIds.contains(prd.getProductId())) {
						throw new Exception(
								"Product " + "'" + prd.getProductName() + "'" + "is already exist in cart ");
					}
				}
				products.addAll(dbProducts);
			}
			cart.setUniqueProductDTO(products);
			cart.setEmail(emailId);
		} else {
			System.out.println("Entered into else block");
			cart = new Cart();
			cart.setUniqueProductDTO(products);
			cart.setEmail(emailId);
			System.out.println("Cart Details ::" + cart);
		}

		return cartService.addToCart(cart);

	}

	@GetMapping("/applyCoupon/{email}/{totalAmount}/{couponCode}")
	public AmountDTO applyCoupon(@PathVariable String email, @PathVariable double totalAmount,
			@PathVariable String couponCode) throws Exception {

		HttpEntity<String> entity = getHttpEntity();

		RestTemplate template = new RestTemplate();

		CouponDTO coupon = template.exchange("http://localhost:2246/coupon/getCoupon/" + couponCode, HttpMethod.GET,
				entity, CouponDTO.class).getBody();

		if (totalAmount < coupon.getAmount()) {
			throw new Exception("Coupon price is high compare to total price try with another coupon");
		}

		System.out.println(" ********** Coupon ********** ::" + coupon);

		List<String> userIds = template.exchange("http://localhost:2246/coupon/getUserEmailList/" + couponCode,
				HttpMethod.GET, entity, List.class).getBody();
		System.out.println("****** User Ids ******** :" + userIds);

		System.out.println("******  Email ******** :" + email);

		if (userIds.contains(email)) {
			double discountedAmount = totalAmount - coupon.getAmount();
			AmountDTO amountDto = new AmountDTO();
			amountDto.setDiscountedAmount(discountedAmount);
			amountDto.setCouponAmount(coupon.getAmount());
			return amountDto;
		}
		throw new Exception("Coupon code is not applicable for this user");
	}

	private HttpEntity<String> getHttpEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return new HttpEntity<>("parameters", headers);
	}

	@PutMapping("/update")
	public String update(@RequestBody ProductDTO p) {
		List<Cart> cartlist = cartService.getCart();
		for (Cart c : cartlist) {
			Set<ProductDTO> prods = c.getUniqueProductDTO();
			Set<ProductDTO> prds = new HashSet<ProductDTO>();
			for (ProductDTO p1 : prods) {
				if (p1.getProductId() != p.getProductId()) {
					prds.add(p1);
				}
				if (p1.getProductId() == p.getProductId()) {
					prds.add(p);
				}
				c.setUniqueProductDTO(prds);
				cartService.addToCart(c);
			}
		}
		return "Cart Updated Successfully";
	}

	@DeleteMapping("/delete/{productId}")
	public String deleteProductUsingProductId(@PathVariable long productId) throws Exception {
		List<Cart> cartList = cartService.getCart();

		if (cartList != null) {
			for (Cart cart : cartList) {
				String emailId = cart.getEmail();

				Set<ProductDTO> prds = cart.getUniqueProductDTO();
				if (!CollectionUtils.isEmpty(prds)) {
					Set<ProductDTO> updatedProducts = prds.stream().filter(prd -> !(productId == prd.getProductId()))
							.collect(Collectors.toSet());

					cart.setUniqueProductDTO(updatedProducts);
					cart.setEmail(emailId);
					cartService.addToCart(cart);
				}
			}
		}
		return "Product removed from cart";
	}
}
