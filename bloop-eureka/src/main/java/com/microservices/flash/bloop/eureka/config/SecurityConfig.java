package com.microservices.flash.bloop.eureka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Configuring HTTP Security
     *     Any request should be authenticated using HTTP Basic   
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf()
            /**
             * Disable CSRF 
             */
            .disable()
            .authorizeRequests()
            .anyRequest()
            /**
             * Authorize all requests, we are not going to be doing pattern matching
             *     Any request that comes into the server should be authenticated (This is going to be a blanket)
             */
            .authenticated()
            .and()
            /**
             * Authenticate all requests that we get with HTTP basic 
             */
            .httpBasic();

        return http.build();
    }

}