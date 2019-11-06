package com.paas.cart.dto;

public class AmountDTO {

	private double discountedAmount;
	private double couponAmount;

	public double getDiscountedAmount() {
		return discountedAmount;
	}

	public void setDiscountedAmount(double discountedAmount) {
		this.discountedAmount = discountedAmount;
	}

	public double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}

}