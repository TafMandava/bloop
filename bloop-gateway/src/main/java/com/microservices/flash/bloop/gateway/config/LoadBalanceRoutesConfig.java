package com.microservices.flash.bloop.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadBalanceRoutesConfig {

    @Bean
    public RouteLocator loadBalanceRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("bloop-censorship-service", 
                    r -> r.path("/api/v1/censor*", "/api/v1/censor/*")
                            .filters(f -> f.circuitBreaker(c -> c.setName("censorshipCircuitBreaker")
                                                                    .setFallbackUri("forward:/censorship-failover")
                                                                    .setRouteId("censorship-failback")
                                                        )
                            )
                            .uri("lb://bloop-censorship-service")
            )
            .route("bloop-client", 
                    r -> r.path("/messages*", "/messages/*", "/api/v1/message*", "/api/v1/message/*")
                            .uri("lb://bloop-client")
            )                                 
            .build();
    }    
    
}
