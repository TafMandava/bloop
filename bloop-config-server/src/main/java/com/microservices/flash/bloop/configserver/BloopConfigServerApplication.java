package com.microservices.flash.bloop.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Mark the class as a config service
 * Use spring-cloud-config-server and @EnableConfigServer 
 */
@EnableConfigServer
@SpringBootApplication
public class BloopConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloopConfigServerApplication.class, args);
	}

}