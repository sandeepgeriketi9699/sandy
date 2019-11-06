package com.paas.sms.tenantservice.document;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;




@Document(collection = "Confirmation_Token")
public class ConfirmationToken {

	
	@Id
	private long tokenid;

	private String confirmationToken;

	private Date createdDate;

	private User user;

	public ConfirmationToken() {
	}

	public ConfirmationToken(User user) {
		this.user = user;
		createdDate = new Date();
		confirmationToken = UUID.randomUUID().toString();
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getTokenid() {
		return tokenid;
	}

	public void setTokenid(long tokenid) {
		this.tokenid = tokenid;
	}
}
