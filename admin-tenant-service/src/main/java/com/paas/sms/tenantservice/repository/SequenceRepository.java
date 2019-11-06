package com.paas.sms.tenantservice.repository;

public interface SequenceRepository {
	
	long getNextSequenceId(String key);
}
