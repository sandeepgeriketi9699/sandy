/*
 * 
 */
package com.iaas.sms.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iaas.sms.dto.LoginDTO;
import com.iaas.sms.dto.TokenDTO;
import com.iaas.sms.security.service.JsonWebTokenAuthenticationService;
import com.iaas.sms.security.service.TokenService;


// TODO: Auto-generated Javadoc
/**
 * The Class AuthenticationController.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    /** The token service. */
    private final TokenService tokenService;
    
 
    
    @Autowired
    JsonWebTokenAuthenticationService jsonWebTokenService;

    /**
     * Instantiates a new authentication controller.
     *
     * @param tokenService the token service
     */
    @Autowired
    public AuthenticationController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Authenticate.
     *
     * @param dto the dto
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody final LoginDTO dto , HttpServletRequest request) {
    	
    	System.out.println("Entered into authentication::");
    	
    	System.out.println("LoginDto userName ::"+dto.getPassword());
    	System.out.println("LoginDto password::"+dto.getUsername());
        final String token = tokenService.getToken(dto.getUsername(), dto.getPassword());
        
        
      
        HttpHeaders headers = new HttpHeaders();
        headers.add("token",token);
        
        System.out.println("Token ::"+token);
        if (token != null) {
        	  System.out.println("Entered into if() block");
            final TokenDTO response = new TokenDTO();
            response.setToken(token);
            return new ResponseEntity<>(response,headers, HttpStatus.OK);
        } 
            return new ResponseEntity<>("Authentication failed", HttpStatus.BAD_REQUEST);
      
    }
}
