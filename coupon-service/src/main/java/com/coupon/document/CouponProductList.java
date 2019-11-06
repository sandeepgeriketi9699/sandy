package com.coupon.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CouponProductList {
	
	@Id
	private String couponCode;
	private List<Product> listOfProducts;
	private List<Long> listOfProductIds;
	
	
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public List<Product> getListOfProducts() {
		return listOfProducts;
	}
	public void setListOfProducts(List<Product> listOfProducts) {
		this.listOfProducts = listOfProducts;
	}
	public List<Long> getListOfProductIds() {
		return listOfProductIds;
	}
	public void setListOfProductIds(List<Long> listOfProductIds) {
		this.listOfProductIds = listOfProductIds;
	}
	
	

}
