package com.coupon.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("com.coupon")
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = "com.coupon")
public class CouponApp1Application {

	public static void main(String[] args) {
		SpringApplication.run(CouponApp1Application.class, args);
	}

}
