package com.paas.sms.tenantservice.document;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 6873826214419483079L;
	
	@Id
	private String id;
	
	private String createdAt;
	
	private String updatedAt;
	
	private LocalDate createdDt;
	
	private LocalDate updatedDt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDate getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(LocalDate createdDt) {
		this.createdDt = createdDt;
	}

	public LocalDate getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(LocalDate updatedDt) {
		this.updatedDt = updatedDt;
	}

	
}
