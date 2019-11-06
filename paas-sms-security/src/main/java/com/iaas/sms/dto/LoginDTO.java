/*
 * 
 */
package com.iaas.sms.dto;

import java.io.Serializable;


// TODO: Auto-generated Javadoc
/**
 * The Class LoginDTO.
 */
public class LoginDTO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4159366809929151486L;

    /** The username. */
    private String username;
    
    /** The password. */
    private String password;

    /**
     * Instantiates a new login DTO.
     */
    public LoginDTO() {
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}
