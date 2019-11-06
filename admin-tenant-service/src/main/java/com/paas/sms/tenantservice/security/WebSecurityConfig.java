package com.paas.sms.tenantservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests()//
				.antMatchers("/paas/registration").permitAll()//
				.antMatchers("/paas/welcome").permitAll()//
				.antMatchers("/users/signin").permitAll()//
				.antMatchers("/users/signup").permitAll()//
				.antMatchers("/users/confirm").permitAll()
				.antMatchers("/users/isEnabled").permitAll()
				.antMatchers("/users/findAllUsers").permitAll()//
				.antMatchers("/users/user/*").permitAll()//
				.antMatchers("/users/user/update/*").permitAll()//
				.antMatchers("/users/isUser/*").permitAll()//
				.antMatchers("/users/tenants/email/*").permitAll()//
				.antMatchers("/users/getCountries").permitAll()//
				.antMatchers("/users/emailExist/**").permitAll()//
				.antMatchers("/users/getCountries/*").permitAll()//
				.antMatchers("/paas/tenants/*").permitAll()//
				.antMatchers("/paas/tenants").permitAll()//
				.antMatchers("/paas/tenants/email/*").permitAll()//
				.antMatchers("/paas/tenants/update/*").permitAll()//
				.antMatchers("/paas/tenants/delete/*").permitAll()//
				.antMatchers("/**").permitAll()//
				.anyRequest().authenticated();

		http.exceptionHandling().accessDeniedPage("/login");
		
	

		http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}

}
