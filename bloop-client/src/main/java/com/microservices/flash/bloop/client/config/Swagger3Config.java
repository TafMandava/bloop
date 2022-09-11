package com.microservices.flash.bloop.client.config;

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
          .title("Bloop Swagger 3 interface documentation")
          .version("1.0.0")
          .description("Bloop API reference for developers")
          .termsOfService("http://swagger.io/terms/")
          .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }	    
    
}
