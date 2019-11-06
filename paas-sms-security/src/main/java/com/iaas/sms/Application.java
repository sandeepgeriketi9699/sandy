/*
 * 
 */
package com.iaas.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.iaas.sms.config.CorsFilter;


// TODO: Auto-generated Javadoc
/**
 * The Class Application.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean =
            new FilterRegistrationBean<>(new CorsFilter());
        corsFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE - 10);
        return corsFilterRegistrationBean;
    }
   

}
