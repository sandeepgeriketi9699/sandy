package com.paas.sms.tenantservice.service;

public interface Sequence {
	
	long getNextSequenceId(String key);
}