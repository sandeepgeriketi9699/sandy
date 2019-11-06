/*
 * 
 */
package com.iaas.sms.security.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.iaas.sms.config.CorsFilter;
import com.iaas.sms.dto.LoginDTO;
import com.iaas.sms.dto.UserDTO;
import com.iaas.sms.exception.model.UserNotFoundException;
import com.iaas.sms.model.User;
import com.iaas.sms.model.UserAuthentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

// TODO: Auto-generated Javadoc
/**
 * The Class JsonWebTokenAuthenticationService.
 */
@Service
public class JsonWebTokenAuthenticationService implements TokenAuthenticationService {

	/** The secret key. */
	@Value("security.token.secret.key")
	private String secretKey;

	/** The user details service. */
	private final UserDetailsService userDetailsService;

	private static final Log log = LogFactory.getLog(JsonWebTokenAuthenticationService.class);

	/**
	 * Instantiates a new json web token authentication service.
	 *
	 * @param userDetailsService the user details service
	 */
	@Autowired
	public JsonWebTokenAuthenticationService(final UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Authenticate.
	 *
	 * @param request the request
	 * @return the authentication
	 * @throws ServletException
	 */
	@Override
	public Authentication authenticate(final HttpServletRequest request) throws ServletException {

		String authHeader = request.getHeader("authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new ServletException("Missing or invalid Authorization header");
		}

		final String token = authHeader.substring(7);
		System.out.println("Token ::" + token);

		final Jws<Claims> tokenData = parseToken(token);
		System.out.println("tokenData ::" + tokenData);
		if (tokenData != null) {
			User user = getUserFromToken(tokenData);
			if (user != null) {
				return new UserAuthentication(user);
			}
		}
		return null;
	}

	/**
	 * Parses the token.
	 *
	 * @param token the token
	 * @return the jws
	 */
	private Jws<Claims> parseToken(final String token) {
		if (token != null) {
			try {
				return Jwts.parser().setSigningKey("Asjfwol2asf123142Ags1k23hnSA36as6f4qQ324FEsvb")
						.parseClaimsJws(token);
			} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
					| IllegalArgumentException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Gets the user from token.
	 *
	 * @param tokenData the token data
	 * @return the user from token
	 */
	private User getUserFromToken(final Jws<Claims> tokenData) {
		try {
			return (User) userDetailsService.loadUserByUsername(tokenData.getBody().get("username").toString());
		} catch (UsernameNotFoundException e) {
			throw new UserNotFoundException("User " + tokenData.getBody().get("username").toString() + " not found");
		}
	}

}
