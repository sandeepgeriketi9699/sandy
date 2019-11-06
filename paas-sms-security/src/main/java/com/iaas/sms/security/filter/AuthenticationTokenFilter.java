/*
 * 
 * 
 * package com.iaas.sms.security.filter;
 * 
 * import java.io.IOException; import java.util.ArrayList; import
 * java.util.List;
 * 
 * import javax.servlet.FilterChain; import javax.servlet.ServletException;
 * import javax.servlet.ServletRequest; import javax.servlet.ServletResponse;
 * import javax.servlet.http.HttpServletRequest;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Qualifier; import
 * org.springframework.context.annotation.Primary; import
 * org.springframework.core.Ordered; import
 * org.springframework.core.annotation.Order; import
 * org.springframework.security.core.Authentication; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.filter.GenericFilterBean;
 * 
 * import com.iaas.sms.security.service.TokenAuthenticationService;
 * 
 * 
 * // TODO: Auto-generated Javadoc
 *//**
	 * The Class AuthenticationTokenFilter.
	 */
/*
 * @Component
 * 
 * @Order(Ordered.HIGHEST_PRECEDENCE) public class AuthenticationTokenFilter
 * extends GenericFilterBean {
 * 
 *//** The authentication service. */
/*
 * @Autowired private final TokenAuthenticationService authenticationService;
 * 
 *//**
	 * Instantiates a new authentication token filter.
	 *
	 * @param authenticationService the authentication service
	 */
/*
 * public AuthenticationTokenFilter(final TokenAuthenticationService
 * authenticationService) { this.authenticationService = authenticationService;
 * }
 * 
 *//**
	 * Do filter.
	 *
	 * @param request     the request
	 * @param response    the response
	 * @param filterChain the filter chain
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 *//*
		 * @Override public void doFilter(final ServletRequest request, final
		 * ServletResponse response, final FilterChain filterChain) throws IOException,
		 * ServletException {
		 * System.out.println("Enetered into AuthenticationTokenFilter");
		 * 
		 * final HttpServletRequest httpRequest = (HttpServletRequest) request;
		 * 
		 * System.out.println("before authenticationService call");
		 * 
		 * filterChain.doFilter(request, response);
		 * 
		 * 
		 * String path = ((HttpServletRequest) request).getServletPath(); List<String>
		 * excludedUrls = new ArrayList<>(); excludedUrls.add("/api/auth");
		 * excludedUrls.add("/api/registration");
		 * excludedUrls.add("/api/registration/confirm");
		 * excludedUrls.add("/api/registration/getCountries");
		 * excludedUrls.add("/api/welcome");
		 * 
		 * //if(!excludedUrls.contains(path)) if(false) { final Authentication
		 * authentication = authenticationService.authenticate(httpRequest);
		 * System.out.println("authentication::"+authentication.getName());
		 * System.out.println("authentication::"+authentication.getAuthorities());
		 * SecurityContextHolder.getContext().setAuthentication(authentication);
		 * filterChain.doFilter(request, response);
		 * SecurityContextHolder.getContext().setAuthentication(null); } else {
		 * filterChain.doFilter(request, response); }
		 * 
		 * } }
		 */