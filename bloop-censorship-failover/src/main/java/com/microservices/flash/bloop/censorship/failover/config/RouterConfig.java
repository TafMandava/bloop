package com.microservices.flash.bloop.censorship.failover.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.microservices.flash.bloop.censorship.web.MessageHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> messageRoute(MessageHandler messageHandler){
        return route(GET("/censorship-failover").and(accept(MediaType.APPLICATION_JSON)), messageHandler::getMessage);
    }

}