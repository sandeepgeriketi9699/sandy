package com.paas.sms.tenantservice.model;

import java.util.List;

import com.paas.sms.tenantservice.document.Authority;

public class SigninDTO {

	private String username;
	
	private String password;
	
	private String token;
	
	private List<Authority> authorities;
	
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public List<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	

	
	
}
