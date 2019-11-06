/*
 * 
 */
package com.iaas.sms.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iaas.sms.converter.factory.ConverterFactory;
import com.iaas.sms.dto.UserDTO;
import com.iaas.sms.model.User;


// TODO: Auto-generated Javadoc
/**
 * The Class ConverterFacade.
 */
@Component
public class ConverterFacade {

    /** The converter factory. */
    @Autowired
    private ConverterFactory converterFactory;

    /**
     * Convert.
     *
     * @param dto the dto
     * @return the user
     */
    public User convert(final UserDTO dto) {
    	
    	System.out.println("Entered into covert()::"+dto);
    	
        return (User) converterFactory.getConverter(dto.getClass()).convert(dto);
    }
}
