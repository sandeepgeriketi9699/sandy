package com.paas.sms.tenantservice.model;

public class Email {
	
	private String subject;
	private String message;
	private String []emails;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String[] getEmails() {
		return emails;
	}
	public void setEmails(String[] emails) {
		this.emails = emails;
	}

}
