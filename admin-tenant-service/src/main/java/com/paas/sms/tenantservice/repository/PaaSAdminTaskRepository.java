package com.paas.sms.tenantservice.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.paas.sms.tenantservice.document.AdminTenant;

@Repository
public interface PaaSAdminTaskRepository extends MongoRepository<AdminTenant, String> {

	AdminTenant findByEmail(String emailId);

}
