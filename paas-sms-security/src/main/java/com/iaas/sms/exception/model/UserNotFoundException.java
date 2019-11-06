/*
 * 
 */
package com.iaas.sms.exception.model;


// TODO: Auto-generated Javadoc
/**
 * The Class UserNotFoundException.
 */
public class UserNotFoundException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2967357473314163159L;

    /**
     * Instantiates a new user not found exception.
     */
    public UserNotFoundException() {
        super();
    }

    /**
     * Instantiates a new user not found exception.
     *
     * @param message the message
     */
    public UserNotFoundException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new user not found exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public UserNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
