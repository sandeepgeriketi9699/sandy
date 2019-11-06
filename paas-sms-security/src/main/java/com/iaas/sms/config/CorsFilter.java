/*
 * 
 */
package com.iaas.sms.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;


// TODO: Auto-generated Javadoc
/**
 * The Class CorsFilter.
 */
@Component
public class CorsFilter implements Filter {
	
	/** The secret key. */
	@Value("security.token.secret.key")
	private String secretKey;

	private static final Log log = LogFactory.getLog(CorsFilter.class);
    /**
     * Inits the.
     *
     * @param filterConfig the filter config
     * @throws ServletException the servlet exception
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Do filter.
     *
     * @param req the req
     * @param res the res
     * @param chain the chain
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
    	
    	log.info("Entered into CorsFilter");
        final HttpServletResponse response = (HttpServletResponse) res;
        
        response.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) req).getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT,DELETE ,OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me,Authorization,x-auth-token");

        log.info("");
        final HttpServletRequest request = (HttpServletRequest) req;
        final String authHeader = request.getHeader("Authorization");
        
        final String authHeader2 = request.getHeader("authorization");
        log.info("Auth Header "+(authHeader2 == null));
        log.info("auth Header "+(authHeader == null));

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        } else {
        	
        	String path = ((HttpServletRequest) req).getServletPath();
        	List<String> excludedUrls = new ArrayList<>();
        	excludedUrls.add("/api/auth");
        	excludedUrls.add("/api/registration");
        	excludedUrls.add("/api/registration/confirm");
        	
        	
        	
        	if(!excludedUrls.contains(path))
    	   {
    		   if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                   throw new ServletException("Missing or invalid Authorization header");
               }

               final String token = authHeader.substring(7);
               
               log.info("*****  Token  ***** ::"+ token);

               try {
                   final Claims claims = Jwts.parser().setSigningKey("Asjfwol2asf123142Ags1k23hnSA36as6f4qQ324FEsvb").parseClaimsJws(token).getBody();
                   request.setAttribute("claims", claims);
               } catch (final SignatureException e) {
                   throw new ServletException("Invalid token");
               }
    	   }

            chain.doFilter(req, res);
        }
    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {

    }
}
