package com.paas.sms.tenantservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.paas.sms.tenantservice.document.ConfirmationToken;



public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {

	ConfirmationToken findByConfirmationToken(String confirmationToken);

	void deleteByUserId(long userId);

	 //void deleteById(long tokenid);

	//void delete(long tokenid);

}
