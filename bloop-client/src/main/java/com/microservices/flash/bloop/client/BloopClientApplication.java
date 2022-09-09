package com.microservices.flash.bloop.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


/** 
 * ComponentScan
 *    Required for finding Spring Beans in other modules
 *    When a Spring Boot application starts, by default it scans packages starting from the packaging directory that the Spring Boot application main class resides
 */
@ComponentScan(basePackages = "com.microservices.flash.bloop")
@EntityScan("com.microservices.flash.bloop.common.data.entities")
@SpringBootApplication
public class BloopClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloopClientApplication.class, args);
	}

}
