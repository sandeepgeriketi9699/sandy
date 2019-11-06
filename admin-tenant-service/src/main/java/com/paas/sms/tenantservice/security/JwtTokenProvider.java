package com.paas.sms.tenantservice.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.paas.sms.tenantservice.document.Authority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	private String secretKey = "Asjfwol2asf123142Ags1k23hnSA36as6f4qQ324FEsvb";

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h
	//3600000; // 1h

	@Autowired
	private MyUserDetails myUserDetails;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		
		System.out.println("Secret Key::"+secretKey);
	}

	
	public String createToken(String username, List<Authority> roles) {

		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))
				.filter(Objects::nonNull).collect(Collectors.toList()));

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);
		
		
		return Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	public String resolveToken(HttpServletRequest req) {

		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	/*
	 * public String getUsername(String token) { return
	 * Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().
	 * getSubject(); }
	 */
	

	public String getUsername(String token) {
		if(validateToken(token)) {
			return  Jwts.parser().setSigningKey(secretKey)
					.parseClaimsJws(token).getBody().getSubject();
		}
		return null;
	}
	
	
	/*
	 * public boolean validateToken(String token) { try {
	 * Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); return true; }
	 * catch (JwtException | IllegalArgumentException e) { throw new
	 * CustomException("Expired or invalid JWT token",
	 * HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */
	
	 public boolean validateToken(String token) {
		if (token != null) {
			try {
				 Jwts.parser().setSigningKey(secretKey)
						.parseClaimsJws(token);
				 return true;
			} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
					| IllegalArgumentException e) {
				return false;
			}
		}
		return false;
	}

}
