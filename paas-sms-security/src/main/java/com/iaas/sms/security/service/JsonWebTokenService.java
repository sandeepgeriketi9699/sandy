/*
 * 
 */
package com.iaas.sms.security.service;

import com.iaas.sms.exception.model.ServiceException;
import com.iaas.sms.model.User;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


// TODO: Auto-generated Javadoc
/**
 * The Class JsonWebTokenService.
 */
@Service
public class JsonWebTokenService implements TokenService {

    /** The token expiration time. */
    private static int tokenExpirationTime = 30;

    /** The token key. */
    @Value("security.token.secret.key")
    private String tokenKey;

    /** The user details service. */
    private final UserDetailsService userDetailsService;

    /**
     * Instantiates a new json web token service.
     *
     * @param userDetailsService the user details service
     */
    @Autowired
    public JsonWebTokenService(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /** 
     * Gets the token.
     *
     * @param username the username
     * @param password the password
     * @return the token
     */
    @Override
    public String getToken(final String username, final String password) {
    	System.out.println("***** Entered into getToken()  ******");
    	
        if (username == null || password == null) {
            return null;
        }
        System.out.println("userDetailsService ::"+userDetailsService);
        final User user = (User) userDetailsService.loadUserByUsername(username);
        System.out.println("User ::"+user);
        Map<String, Object> tokenData = new HashMap<>();
        System.out.println("asPassword ::"+password.equals(user.getPassword()));
        if (password.equals(user.getPassword())) {
        	System.out.println("Enetered into if() block");
            tokenData.put("clientType", "user");
            tokenData.put("userID", user.getId());
            tokenData.put("username", user.getUsername());
            tokenData.put("token_create_date", LocalDateTime.now());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, tokenExpirationTime);
            tokenData.put("token_expiration_date", calendar.getTime());
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setExpiration(calendar.getTime());
            jwtBuilder.setClaims(tokenData);
            return jwtBuilder.signWith(SignatureAlgorithm.HS512, "Asjfwol2asf123142Ags1k23hnSA36as6f4qQ324FEsvb").compact();

        } else {
            throw new ServiceException("Authentication error", this.getClass().getName());
        }
    }

    /**
     * Sets the token expiration time.
     *
     * @param tokenExpirationTime the new token expiration time
     */
    public static void setTokenExpirationTime(final int tokenExpirationTime) {
        JsonWebTokenService.tokenExpirationTime = tokenExpirationTime;
    }
}
