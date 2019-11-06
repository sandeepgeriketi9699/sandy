/*
 * 
 */
package com.iaas.sms.dto;

import java.io.Serializable;


// TODO: Auto-generated Javadoc
/**
 * The Class TokenDTO.
 */
public class TokenDTO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6710061358371752955L;

    /** The token. */
    private String token;

    /**
     * Instantiates a new token DTO.
     */
    public TokenDTO() {
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token the new token
     */
    public void setToken(final String token) {
        this.token = token;
    }
}
