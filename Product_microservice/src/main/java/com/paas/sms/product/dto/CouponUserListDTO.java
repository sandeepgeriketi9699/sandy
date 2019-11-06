package com.paas.sms.product.dto;

import java.util.List;

import org.springframework.data.annotation.Id;


public class CouponUserListDTO {

	@Id
	private String couponCode;
	private String emailId;
	private List<String> userEmailList;

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<String> getUserEmailList() {
		return userEmailList;
	}

	public void setUserEmailList(List<String> userEmailList) {
		this.userEmailList = userEmailList;
	}

}
