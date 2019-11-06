/*
 * 
 */
package com.iaas.sms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.iaas.sms.model.User;


// TODO: Auto-generated Javadoc
/**
 * The Interface UserRepository.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find by username.
     *
     * @param userName the user name
     * @return the user
     */
    User findByUsername(final String userName);

	Optional<User> findById(String id);
	
	String deleteByUsername(String username);
	 
}
