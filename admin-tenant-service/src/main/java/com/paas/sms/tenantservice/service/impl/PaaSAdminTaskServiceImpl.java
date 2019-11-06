package com.paas.sms.tenantservice.service.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paas.sms.tenantservice.document.AdminTenant;
import com.paas.sms.tenantservice.repository.PaaSAdminTaskRepository;
import com.paas.sms.tenantservice.service.PaaSAdminTaskService;

@Service
public class PaaSAdminTaskServiceImpl implements PaaSAdminTaskService {

	@Autowired
	private PaaSAdminTaskRepository paaSAdminTaskRepository;

	@Override
	public List<AdminTenant> findAll() {
		return paaSAdminTaskRepository.findAll();
	}

	@Override
	public AdminTenant findById(String tenantId) {
		Optional optionalUser = paaSAdminTaskRepository.findById(tenantId);
		if(optionalUser.isPresent())
			return (AdminTenant) optionalUser.get();
		else
			return null;
	}

	@Override
	public AdminTenant findByEmail(String emailId) {

		AdminTenant user = paaSAdminTaskRepository.findByEmail(emailId);
		if(user!=null) {
			return user;
		}
		return null;
	}
	
	@Override
	public AdminTenant save(@Valid AdminTenant tenant) {
		return paaSAdminTaskRepository.save(tenant);
	}

	@Override
	public void delete(AdminTenant tenant) {
		paaSAdminTaskRepository.delete(tenant);
	}

	

}
