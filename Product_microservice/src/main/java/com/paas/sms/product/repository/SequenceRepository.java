package com.paas.sms.product.repository;

public interface SequenceRepository {
	
	long getNextSequenceId(String key);
}
