/*
 * 
 */
package com.iaas.sms.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


// TODO: Auto-generated Javadoc
/**
 * The Class BaseEntity.
 */
@Document
public class BaseEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8571261118900116242L;

    /** The id. */
    @Id
    private String id;
    
    /** The created at. */
    private String createdAt;
    
    /** The updated at. */
    private String updatedAt;
    
    /** The created dt. */
    private LocalDate createdDt;
    
    /** The updated dt. */
    private LocalDate updatedDt;

    /**
     * Instantiates a new base entity.
     */
    public BaseEntity() {
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Gets the created at.
     *
     * @return the created at
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created at.
     *
     * @param createdAt the new created at
     */
    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the updated at.
     *
     * @return the updated at
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the updated at.
     *
     * @param updatedAt the new updated at
     */
    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

	/**
	 * Gets the created dt.
	 *
	 * @return the createdDt
	 */
	public LocalDate getCreatedDt() {
		return createdDt;
	}

	/**
	 * Sets the created dt.
	 *
	 * @param createdDt the createdDt to set
	 */
	public void setCreatedDt(LocalDate createdDt) {
		this.createdDt = createdDt;
	}

	/**
	 * Gets the updated dt.
	 *
	 * @return the updatedDt
	 */
	public LocalDate getUpdatedDt() {
		return updatedDt;
	}

	/**
	 * Sets the updated dt.
	 *
	 * @param updatedDt the updatedDt to set
	 */
	public void setUpdatedDt(LocalDate updatedDt) {
		this.updatedDt = updatedDt;
	}
    
    
}
