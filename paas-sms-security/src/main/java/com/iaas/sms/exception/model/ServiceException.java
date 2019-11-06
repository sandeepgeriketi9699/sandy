/*
 * 
 */
package com.iaas.sms.exception.model;


// TODO: Auto-generated Javadoc
/**
 * The Class ServiceException.
 */
public class ServiceException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8658131859261427602L;

    /** The service. */
    private String service;

    /**
     * Instantiates a new service exception.
     *
     * @param service the service
     */
    public ServiceException(final String service) {
        super();
        this.service = service;
    }

    /**
     * Instantiates a new service exception.
     *
     * @param message the message
     * @param service the service
     */
    public ServiceException(final String message, final String service) {
        super(message);
        this.service = service;
    }

    /**
     * Instantiates a new service exception.
     *
     * @param message the message
     * @param cause the cause
     * @param service the service
     */
    public ServiceException(final String message, final Throwable cause,
                            final String service) {
        super(message, cause);
        this.service = service;
    }

    /**
     * Gets the service.
     *
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * Sets the service.
     *
     * @param service the new service
     */
    public void setService(final String service) {
        this.service = service;
    }
}
