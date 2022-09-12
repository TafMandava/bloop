package com.microservices.flash.bloop.censorshipservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class Swagger3Config {

    @Bean
    public OpenAPI customOpenAPI() {
     return new OpenAPI()
          .info(new Info()
          .title("Word Censorship Service Swagger 3 interface documentation")
          .version("1.0.0")
          .description("Word Censorship Service API reference for developers")
          .termsOfService("http://swagger.io/terms/")
          .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }	    
    
}

