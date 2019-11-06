/*
 * 
 */
package com.iaas.sms.model;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
@Document(collection= "userMaster")
public class User extends BaseEntity implements UserDetails {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7954325925563724664L;

    
    /** The authorities. */
    private List<Authority> authorities;
    
    private String firstname;
    
    private String lastname;
    
    /** The username. */
    private String username;
    
    /** The password. */
    private String password;
    
    /** The email. */
    private String email;
	
	/** The mobilenumber. */
	private String mobilenumber;
	
	public String getExtensionNumber() {
		return extensionNumber;
	}


	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}


	private String extensionNumber;
	
	/** The password confirm. */
	private String passwordConfirm;
	
    /** The account non expired. */
    private boolean accountNonExpired;
    
    /** The account non locked. */
    private boolean accountNonLocked;
    
    /** The credentials non expired. */
    private boolean credentialsNonExpired;
    
    /** The is enabled. */
    private boolean isEnabled;

    private String country;
    
    private String role;
    /**
     * Gets the authorities.
     *
     * @return the authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    
    public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	/**
     * Gets the email.
     *
     * @return the email
     */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the mobilenumber.
	 *
	 * @return the mobilenumber
	 */
	public String getMobilenumber() {
		return mobilenumber;
	}

	/**
	 * Sets the mobilenumber.
	 *
	 * @param mobilenumber the mobilenumber to set
	 */
	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	/**
	 * Gets the password confirm.
	 *
	 * @return the passwordConfirm
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	/**
	 * Sets the password confirm.
	 *
	 * @param passwordConfirm the passwordConfirm to set
	 */
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	@Override
    public String getPassword() {
        return password;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Checks if is account non expired.
     *
     * @return true, if is account non expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Checks if is account non locked.
     *
     * @return true, if is account non locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Checks if is credentials non expired.
     *
     * @return true, if is credentials non expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Sets the authorities.
     *
     * @param authorities the new authorities
     */
    public void setAuthorities(final List<Authority> authorities) {
        this.authorities = authorities;
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
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Sets the account non expired.
     *
     * @param accountNonExpired the new account non expired
     */
    public void setAccountNonExpired(final boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * Sets the account non locked.
     *
     * @param accountNonLocked the new account non locked
     */
    public void setAccountNonLocked(final boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * Sets the credentials non expired.
     *
     * @param credentialsNonExpired the new credentials non expired
     */
    public void setCredentialsNonExpired(final boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    public void setEnabled(final boolean enabled) {
        isEnabled = enabled;
    }


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}

	
    
}
