/*
 * 
 */
package com.iaas.sms.security.service;


// TODO: Auto-generated Javadoc
/**
 * The Interface TokenService.
 */
public interface TokenService {

    /**
     * Gets the token.
     *
     * @param username the username
     * @param password the password
     * @return the token
     */
    String getToken(String username, String password);
}
