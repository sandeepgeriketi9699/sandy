/*
 * 
 */
package com.iaas.sms.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// TODO: Auto-generated Javadoc
/**
 * The Class RestAuthenticationEntryPoint.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commence.
     *
     * @param request the request
     * @param response the response
     * @param e the e
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException e)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
