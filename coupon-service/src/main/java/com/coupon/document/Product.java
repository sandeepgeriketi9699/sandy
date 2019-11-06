package com.coupon.document;

public class Product {
	private Long productId;
	private String productName;
	private double productPrice;
	private String productDescription;
	private String productDuration;

	

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productPrice=" + productPrice
				+ ", productDescription=" + productDescription + ", productDuration=" + productDuration + "]";
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductID(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductDuration() {
		return productDuration;
	}

	public void setProductDuration(String productDuration) {
		this.productDuration = productDuration;
	}

}
