package com.iaas.sms.multitenant.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iaas.sms.multitenant.model.Tenant;
import com.iaas.sms.multitenant.repository.MultitenancyRepository;

@Service
public class MultitenancyService {

    @Autowired
    private MultitenancyRepository multitenancyRepository;

    public Tenant addTenant(Tenant person){
    	return multitenancyRepository.save(person);
    }

//    public Tenant getTenant(Integer id){
//        return multitenancyRepository.findById(id).orElse(null);
//    }
    
}