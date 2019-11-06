package com.coupon.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CouponUserList {

	@Id
	private String couponCode;
	private String emailId;
	private List<String> userEmailList;
	private List<UserDTO> users;

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}

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
