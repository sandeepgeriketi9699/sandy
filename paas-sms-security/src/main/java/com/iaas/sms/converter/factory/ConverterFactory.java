/*
 * 
 */
package com.iaas.sms.converter.factory;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.iaas.sms.converter.dto.UserDTOConverter;
import com.iaas.sms.dto.UserDTO;


// TODO: Auto-generated Javadoc
/**
 * A factory for creating Converter objects.
 */
@Component
public class ConverterFactory {

    /** The converters. */
    private Map<Object, Converter> converters;

    /**
     * Instantiates a new converter factory.
     */
    public ConverterFactory() {

    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        converters = new HashMap<>();
        converters.put(UserDTO.class, new UserDTOConverter());
    }

    /**
     * Gets the converter.
     *
     * @param type the type
     * @return the converter
     */
    public Converter getConverter(final Object type) {
    	System.out.println("********* getConverter() :: type ::*******::"+type);
        return converters.get(type);
    }
}
