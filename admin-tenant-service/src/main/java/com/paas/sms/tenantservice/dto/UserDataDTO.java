package com.paas.sms.tenantservice.dto;

import java.util.List;

import com.paas.sms.tenantservice.document.Authority;

public class UserDataDTO {
	
	private Integer id;
	
	private String username;
	  
	private String email;
	  
	List<Authority> roles;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Authority> getRoles() {
		return roles;
	}

	public void setRoles(List<Authority> roles) {
		this.roles = roles;
	}
	  
}
