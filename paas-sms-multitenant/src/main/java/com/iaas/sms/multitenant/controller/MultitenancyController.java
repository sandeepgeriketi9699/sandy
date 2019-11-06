package com.iaas.sms.multitenant.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iaas.sms.multitenant.model.Tenant;
import com.iaas.sms.multitenant.service.MultitenancyService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/iaasmultitenant")
public class MultitenancyController {
	private static final Logger logger = LoggerFactory.getLogger(MultitenancyController.class);
	@Autowired
	private MultitenancyService multitenancyService;

	@Autowired
	private HttpServletRequest request;

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Tenant> createCollection(@RequestBody Tenant t) {
		String name = t.getUsername();
		String subName = name.substring(0, 3);
		LocalDateTime current = LocalDateTime.now();
				// to print in a particular format
		DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		String formatedDateTime = current.format(format);
		String nameAndDate = subName + formatedDateTime;
		request.setAttribute("name", nameAndDate);
		Tenant tenant = multitenancyService.addTenant(t);
		if (tenant != null) {
			ResponseEntity<Tenant> response = new ResponseEntity<Tenant>(tenant, HttpStatus.CREATED);
			return response;
		}

		return null;
	}

}