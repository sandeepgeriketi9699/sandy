/*
 * 
 */
package com.iaas.sms.security.service;

import org.springframework.security.core.Authentication;

import com.iaas.sms.dto.LoginDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


// TODO: Auto-generated Javadoc
/**
 * The Interface TokenAuthenticationService.
 */
public interface TokenAuthenticationService {

    /**
     * Authenticate.
     *
     * @param request the request
     * @return the authentication
     * @throws ServletException 
     */
    Authentication authenticate(HttpServletRequest request) throws ServletException;
}
