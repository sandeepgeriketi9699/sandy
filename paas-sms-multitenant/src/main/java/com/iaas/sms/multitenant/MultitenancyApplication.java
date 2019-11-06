package com.iaas.sms.multitenant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableSwagger2
@ComponentScan("com.iaas")
public class MultitenancyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultitenancyApplication.class, args);
	}
	
	@Bean
    public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters(getGlobalParams())
        		.select()                                  
	            .apis(RequestHandlerSelectors.any())              
	            .paths(PathSelectors.any())
	            .build(); 
	}

	private List<Parameter> getGlobalParams() {
		List<Parameter> parameters = new ArrayList<>();
		
		parameters.add(new ParameterBuilder().name("X-TENANT-ID")
								             .description("Tenant")
								             .modelRef(new ModelRef("string"))
								             .parameterType("header")
								             .required(true)
								             .build());
		return parameters;
	}  

}
