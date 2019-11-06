package com.paas.license.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("com.paas")
@EnableMongoRepositories(basePackages="com.paas.license.repository")
@EnableDiscoveryClient
public class LicenseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenseServiceApplication.class, args);
	}

}
