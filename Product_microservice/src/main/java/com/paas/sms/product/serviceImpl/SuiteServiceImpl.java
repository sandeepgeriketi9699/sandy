package com.paas.sms.product.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paas.sms.product.model.Suite;
import com.paas.sms.product.repository.SuiteRepository;
import com.paas.sms.product.service.SuiteService;

@Service
public class SuiteServiceImpl implements SuiteService {

	@Autowired
	SuiteRepository suiteRepository;

	@Override
	public void addSuite(Suite suite) {
		suiteRepository.save(suite);
	}

}
