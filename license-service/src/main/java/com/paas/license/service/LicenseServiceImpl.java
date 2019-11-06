package com.paas.license.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paas.license.document.License;
import com.paas.license.dto.ProductDTO;
import com.paas.license.repository.LicenseRepository;

@Service
public class LicenseServiceImpl implements LicenseService {

	@Autowired
	LicenseRepository licenseRepository;

	public License activateLicense(License license) {

		License result = licenseRepository.save(license);
		if (result != null) {
			return result;
		}
		return null;

	}

	@Override
	public License findByEmail(String emailId) {
		License license = licenseRepository.findByEmail(emailId);
		if (license != null) {
			return license;
		}
		return null;
	}

	@Override
	public Set<ProductDTO> trailfindByEmail(String emailId) {
		License license = licenseRepository.findByEmail(emailId);
		Set<ProductDTO> trialProducts = null;
		if (license != null) {
			Set<ProductDTO> products = license.getUniqueProducts();
			trialProducts = new HashSet();
			if (products != null) {
				for (ProductDTO prd : products) {
					if (prd.isTrial())
						trialProducts.add(prd);
				}

			}
			return trialProducts;
		}
		// throw new CustomException("No license activated for this user :" + emailId,
		// HttpStatus.EXPECTATION_FAILED);

		return null;

	}

	@Override
	public Set<ProductDTO> subscribedfindByEmail(String emailId) {
		License license = licenseRepository.findByEmail(emailId);
		Set<ProductDTO> subscribedProducts = null;
		if (license != null) {
			Set<ProductDTO> products = license.getUniqueProducts();
			subscribedProducts = new HashSet();
			if (products != null) {
				for (ProductDTO prd : products) {
					if (!prd.isTrial())
						subscribedProducts.add(prd);
				}

			}
		}
		return subscribedProducts;
	}

	@Override
	public List<License> getAllActiveLicenses() {

		return licenseRepository.findAll();
	}

	@Override
	public String deleteLicenseByEmail(License license) {
		licenseRepository.delete(license);
		return null;
	}

}
