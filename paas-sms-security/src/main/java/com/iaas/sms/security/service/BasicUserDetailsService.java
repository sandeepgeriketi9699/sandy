/*
 * 
 */
package com.iaas.sms.security.service;

import com.iaas.sms.model.User;
import com.iaas.sms.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// TODO: Auto-generated Javadoc
/**
 * The Class BasicUserDetailsService.
 */
@Service
public class BasicUserDetailsService implements UserDetailsService {

    /** The user service. */
    private final UserService userService;

    /**
     * Instantiates a new basic user details service.
     *
     * @param userService the user service
     */
    @Autowired
    public BasicUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Load user by username.
     *
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException the username not found exception
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User with username:" + username + " not found");
        }
    }
    
    public boolean isUserExist(String userName) {
    	
    	 final User user = userService.findByUsername(userName);
    	 if(user!=null) {
    		 return true;
    	 }
    	 return false;
    }
}
