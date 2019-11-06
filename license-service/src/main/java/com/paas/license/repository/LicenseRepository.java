package com.paas.license.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.paas.license.document.License;

public interface LicenseRepository extends MongoRepository<License, String> {

	License findByEmail(String email);
}
