package com.microservices.flash.bloop.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//@Profile("localhostroutes")
//@Configuration
public class LocalHostRoutesConfig {
    
    //@Bean
    public RouteLocator localHostRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("bloop-censorship-service", 
                    r -> r.path("/api/v1/censor*", "/api/v1/censor/*")
                            .uri("http://localhost:8180")
            )
            .route("bloop-client", 
                    r -> r.path("/api/v1/message*", "/api/v1/message/*")
                            .uri("http://localhost:8280")
            )                      
            .build();
    }
}