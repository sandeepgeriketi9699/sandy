package com.sandeep.application5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.sandeep")
public class Application5Application {

	public static void main(String[] args) {
		SpringApplication.run(Application5Application.class, args);
	}

}
