/*
 * 
 */
package com.iaas.sms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


// TODO: Auto-generated Javadoc
/**
 * The Class IaaSwelcomeController.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/welcome")
public class IaaSwelcomeController {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(IaaSwelcomeController.class);

    /**
     * Say welcome.
     *
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> sayWelcome() {
        return new ResponseEntity<>("Welcome IaaS Home page", HttpStatus.OK);
    }
}
