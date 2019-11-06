package com.paas.sms.tenantservice.service;

import java.util.List;

import javax.validation.Valid;

import com.paas.sms.tenantservice.document.AdminTenant;

public interface PaaSAdminTaskService {

	public List<AdminTenant> findAll();

	public AdminTenant findById(String tenantId);

	public AdminTenant findByEmail(String emailId);
	
	public AdminTenant save(@Valid AdminTenant tenant);

	public void delete(AdminTenant tenant);
	
}
