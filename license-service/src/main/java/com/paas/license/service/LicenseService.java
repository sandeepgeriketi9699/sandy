package com.paas.license.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.paas.license.document.License;
import com.paas.license.dto.ProductDTO;

public interface LicenseService {

	License activateLicense(License license);

	License findByEmail(String emailId);

	List<License> getAllActiveLicenses();

	Set<ProductDTO> subscribedfindByEmail(String emailId);

	String deleteLicenseByEmail(License license);

	Set<ProductDTO> trailfindByEmail(String emailId);

}
