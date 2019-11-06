package com.paas.sms.tenantservice.document;

import java.util.Date;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;


public class AdminTenant extends BaseEntity  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long tId;
	
	@Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
	private String tName;
	
	
	private String tType;
	
	private Date signupDate;
	
	private String email;
	
	public AdminTenant() {
		
	}

	public AdminTenant(long tId, String tName, String tType, Date signupDate, String email) {
		super();
		this.tId = tId;
		this.tName = tName;
		this.tType = tType;
		this.signupDate = signupDate;
		this.email = email;
	}

	public long gettId() {
		return tId;
	}

	public void settId(long tId) {
		this.tId = tId;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public String gettType() {
		return tType;
	}

	public void settType(String tType) {
		this.tType = tType;
	}

	public Date getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
