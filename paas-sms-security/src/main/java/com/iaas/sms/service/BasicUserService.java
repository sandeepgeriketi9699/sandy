/*
 * 
 */
package com.iaas.sms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


import com.iaas.sms.model.User;

import com.iaas.sms.repository.UserRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicUserService.
 */
@Service
public class BasicUserService implements UserService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(BasicUserService.class);

	/** The repository. */
	private final UserRepository repository;

	

	/**
	 * Instantiates a new basic user service.
	 *
	 * @param repository the repository
	 */
	@Autowired
	public BasicUserService(final UserRepository repository) {
		this.repository = repository;
	}

	/**
	 * Creates the.
	 *
	 * @param user the user
	 * @return the user
	 * @throws Exception
	 */
	@Override
	public User create(final User user) throws Exception {
		logger.info("----------------------------->" + user);
		
		user.setCreatedAt(String.valueOf(LocalDateTime.now()));

		return repository.save(user);
	}

	/**
	 * Find.
	 *
	 * @param id the id
	 * @return the user
	 */
	@Override
	public User find(final String id) {
		return repository.findById(id).orElse(null);
	}

	/**
	 * Find by username.
	 *
	 * @param userName the user name
	 * @return the user
	 */
	@Override
	public User findByUsername(final String userName) {
		return repository.findByUsername(userName);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@Override
	public List<User> findAll() {
		return repository.findAll();
	}

	/**
	 * Update.
	 *
	 * @param id   the id
	 * @param user the user
	 * @return the user
	 */
	@Override
	public User update(final String id, final User user) {
		user.setId(id);

		final User saved = repository.findById(id).orElse(null);

		if (saved != null) {
			user.setCreatedAt(saved.getCreatedAt());
			user.setUpdatedAt(String.valueOf(LocalDateTime.now()));
		} else {
			user.setCreatedAt(String.valueOf(LocalDateTime.now()));
		}
		repository.save(user);
		return user;
	}

	/**
	 * Delete.
	 *
	 * @param id the id
	 * @return the string
	 */
	@Override
	public String delete(String username) {
		//repository.delete(repository.findById(id).orElse(null));
		repository.deleteByUsername(username);
		return "User deleted with UserName  :"+username;
	}
}
