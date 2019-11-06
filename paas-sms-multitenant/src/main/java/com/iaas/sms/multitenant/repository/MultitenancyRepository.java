package com.iaas.sms.multitenant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.iaas.sms.multitenant.model.Tenant;

public interface MultitenancyRepository extends MongoRepository<Tenant,Integer> {
}
