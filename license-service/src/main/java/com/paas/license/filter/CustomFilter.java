package com.paas.license.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class CustomFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		servletResponse.setHeader("Access-Control-Allow-Origin", "*");
		servletResponse.setHeader("Access-Control-Allow-Methods", "GET,OPTIONS,POST,DELETE,PUT");
		servletResponse.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization");
		servletResponse.setHeader("Access-Control-Allow-Credentials", "false");
		servletResponse.setHeader("Access-Control-Max-Age", "3600");

		System.out.println("URL ::" + servletRequest.getRequestURL());
		System.out.println("URI ::" + servletRequest.getRequestURI().substring(31));

		Enumeration<String> headerNames = servletRequest.getHeaderNames();

		HttpSession session = servletRequest.getSession();
		session.setAttribute("token", servletRequest.getHeader("authorization"));

		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				System.out.println("Header: " + name + " value:" + servletRequest.getHeader(name));
			}
		}
		HttpHeaders headers = new HttpHeaders();
		List<String> urls = new ArrayList();
		String emailId=servletRequest.getRequestURI().substring(32);
		System.out.println("emailId ::"+servletRequest.getRequestURI().substring(32));
		urls.add("/paaslicense/license/deactivate/"+emailId);
		
		if (urls.contains(servletRequest.getRequestURI()) || servletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
			chain.doFilter(servletRequest, servletResponse);
		} else {
			String token = servletRequest.getHeader("Authorization");
			System.out.println("Token Value From Header :" + token);

			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			RestTemplate r = new RestTemplate();

			String value = r.exchange("http://localhost:2137/admintask/users/nameByToken/" + token, HttpMethod.GET,
					entity, String.class).getBody();
			System.out.println("String value ::" + value);
			if (!value.equalsIgnoreCase("Un Authorized User")
					|| servletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
				chain.doFilter(servletRequest, servletResponse);
			} else {
				throw new ServletException("********* UnAuthorized User ************");
			}
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("########## Initiating Custom filter ##########");
	}

}
