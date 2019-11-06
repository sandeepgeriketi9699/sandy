package com.paas.sms.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoRepositories("com.paas.sms.product.repository")
@EnableDiscoveryClient
@SpringBootApplication (exclude={DataSourceAutoConfiguration.class})
//@ComponentScan("com.paas.sms.product.controller")
public class ProductDirAccessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductDirAccessServiceApplication.class, args);
	}
}
