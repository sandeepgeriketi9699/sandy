/*
 * 
 */
package com.iaas.sms.service;

import java.util.List;

import com.iaas.sms.model.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserService.
 */
public interface UserService {

	/**
	 * Creates the.
	 *
	 * @param object the object
	 * @return the user
	 * @throws Exception
	 */
	User create(User object) throws Exception;

	/**
	 * Find.
	 *
	 * @param id the id
	 * @return the user
	 */
	User find(String id);

	/**
	 * Find by username.
	 *
	 * @param userName the user name
	 * @return the user
	 */
	User findByUsername(String userName);

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<User> findAll();

	/**
	 * Update.
	 *
	 * @param id     the id
	 * @param object the object
	 * @return the user
	 */
	User update(String id, User object);

	/**
	 * Delete.
	 *
	 * @param userName the id
	 * @return the string
	 */
	String delete(String userName);
	

}
