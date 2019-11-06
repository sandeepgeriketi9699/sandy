/*
 * 
 */
package com.iaas.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

// TODO: Auto-generated Javadoc
/**
 * The Class SecurityConfig.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/** The token authentication service. */
	/*
	 * @Autowired private final TokenAuthenticationService
	 * tokenAuthenticationService;
	 */
	/**
	 * Instantiates a new security config.
	 *
	 * @param tokenAuthenticationService the token authentication service
	 */

	/*
	 * protected SecurityConfig(final TokenAuthenticationService
	 * tokenAuthenticationService) { super(); this.tokenAuthenticationService =
	 * tokenAuthenticationService; }
	 */

	/* *//**
			 * Configure.
			 *
			 * @param http the http
			 * @throws Exception the exception
			 */

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		System.out.println("Enterd into configure() in SecurityConfig");

		http.authorizeRequests().antMatchers("/api/auth").permitAll().antMatchers("/api/registration").permitAll()
				.antMatchers("/admintask/iaasmultitenant")
				.permitAll().antMatchers("/api/registration/deleteUser/*").permitAll()
				.antMatchers("/**").permitAll()//
				.anyRequest().authenticated().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
				 and().csrf().disable();

		http.exceptionHandling().accessDeniedPage("/login");

		/*
		 * .anyRequest().authenticated().and()
		 * 
		 * .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
		 * and().csrf().disable();
		 */
	}

}
