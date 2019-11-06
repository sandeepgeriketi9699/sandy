/*
 * 
 */
package com.iaas.sms.model;

import org.springframework.security.core.GrantedAuthority;


// TODO: Auto-generated Javadoc
/**
 * The Enum Authority.
 */
public enum Authority implements GrantedAuthority {
    
    /** The role user. */
    ROLE_USER,
    ROLE_ADMIN;
   // ANONYMOUS;

    /**
     * Gets the authority.
     *
     * @return the authority
     */
    @Override
    public String getAuthority() {
        return this.name();
    }
}
