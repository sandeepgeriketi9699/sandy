/*
 * 
 */
package com.iaas.sms.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


// TODO: Auto-generated Javadoc
/**
 * The Class UserAuthentication.
 */
public class UserAuthentication implements Authentication {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7170337143687707450L;

    /** The user. */
    private final User user;
    
    /** The authenticated. */
    private boolean authenticated = true;

    /**
     * Instantiates a new user authentication.
     *
     * @param user the user
     */
    public UserAuthentication(final User user) {
        this.user = user;
    }

    /**
     * Gets the authorities.
     *
     * @return the authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    /**
     * Gets the credentials.
     *
     * @return the credentials
     */
    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    /**
     * Gets the details.
     *
     * @return the details
     */
    @Override
    public Object getDetails() {
        return user;
    }

    /**
     * Gets the principal.
     *
     * @return the principal
     */
    @Override
    public Object getPrincipal() {
        return user.getUsername();
    }

    /**
     * Checks if is authenticated.
     *
     * @return true, if is authenticated
     */
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Sets the authenticated.
     *
     * @param authenticated the new authenticated
     * @throws IllegalArgumentException the illegal argument exception
     */
    @Override
    public void setAuthenticated(final boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return user.getUsername();
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }
}
