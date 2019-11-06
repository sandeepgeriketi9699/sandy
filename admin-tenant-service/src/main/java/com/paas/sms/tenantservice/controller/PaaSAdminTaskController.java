package com.paas.sms.tenantservice.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paas.sms.tenantservice.document.AdminTenant;
import com.paas.sms.tenantservice.exceptions.ExceptionResponse;
import com.paas.sms.tenantservice.repository.SequenceRepository;
import com.paas.sms.tenantservice.service.PaaSAdminTaskService;

@RestController
@RequestMapping("/paas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaaSAdminTaskController {

	@Autowired
	PaaSAdminTaskService paaSAdminTaskService;

	@Autowired
	private SequenceRepository sequenceRepository;

	private static final String HOSTING_SEQ_KEY = "hosting";

	private static final Logger logger = LoggerFactory.getLogger(PaaSAdminTaskController.class);

	@GetMapping("/tenants")
	public List<AdminTenant> getAllTenants() {

		try {
			logger.info("called");
			return paaSAdminTaskService.findAll();
		} catch (Exception ex) {
			logger.info("Error", ex);
		}
		return new ArrayList<>();

	}

	@PostMapping("/tenants/create")
	public AdminTenant createTenant(@Valid @RequestBody AdminTenant tenant) {
		tenant.settId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
		return paaSAdminTaskService.save(tenant);

	}

	@GetMapping("/tenants/{id}")
	public ResponseEntity<AdminTenant> getTenantsById(@PathVariable(value = "id") String tenantId) {
		AdminTenant tenant = paaSAdminTaskService.findById(tenantId);
		return ResponseEntity.ok().body(tenant);
	}

	@GetMapping("/tenants/email/{email}")
	public ResponseEntity<AdminTenant> findByEmail(@PathVariable(value = "email") String emailId) {
		AdminTenant tenant = paaSAdminTaskService.findByEmail(emailId);
		return ResponseEntity.ok().body(tenant);
	}

	@PutMapping("/tenants/update/{id}")
	public ResponseEntity<AdminTenant> updateTenant(@PathVariable(value = "id") String tenantId,
			@Valid @RequestBody AdminTenant tenantDetails) {
		AdminTenant tenant = paaSAdminTaskService.findById(tenantId);

		if (tenantDetails.gettId() != 0) {
			tenant.settId(tenantDetails.gettId());
		}
		if (tenantDetails.gettName() != null) {
			tenant.settName(tenantDetails.gettName());
		}
		if (tenantDetails.gettType() != null) {
			tenant.settType(tenantDetails.gettType());
		}
		if (tenantDetails.getSignupDate() != null) {
			tenant.setSignupDate(tenantDetails.getSignupDate());
		}
		if (tenantDetails.getEmail() != null) {
			tenant.setEmail(tenantDetails.getEmail());
		}

		AdminTenant updatedTenant = paaSAdminTaskService.save(tenant);
		return ResponseEntity.ok(updatedTenant);

	}

	@DeleteMapping("/tenants/delete/{id}")
	public ResponseEntity<?> deleteTenant(@PathVariable(value = "id") String tenantId) {

		AdminTenant tenant = paaSAdminTaskService.findById(tenantId);

		paaSAdminTaskService.delete(tenant);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(new ExceptionResponse(new Date(), "Record Deleted", tenantId));

	}
}
