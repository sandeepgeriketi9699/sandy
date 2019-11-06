package com.paas.sms.tenantservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paas.sms.tenantservice.document.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

	User findByUsername(String username);


	boolean existsByUsername(String username);

	@Transactional
	String deleteByUsername(String username);


	User findByEmail(String emailId);
	void delete(User user);


	//boolean existsByUserMail(String email);


	/* boolean existsByUserMail(String email); */

}
