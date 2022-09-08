package com.microservices.flash.bloop.configserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**
 *  Create a Spring Security Class by extending WebSecurityConfigurerAdapter and override configure() methods
 *  Authentication is required by default
 *  Default login page is good for quick test
 *  Show Spring-generated error message in custom login page using attrubute SPRING_SECURITY_LAST_EXCEPTION is session
 *  Every POST request must include _crsf toke -> prevent Cross Site Request Forgery (CSRF) attacks
 *  Ignore authentication for static resources (images, CSS, JS, e.t.c)
 *  Logout request must be sent in HTTP POST
 *  Create a class of type UserDetaks ti represent the currently logged-in user
 *  Use @AuthenticationPrincipal to get the UserDetails object representing the authenticated user: @AuthenticationPrincipal StoreUserDetails loggedUser
 *  Default cookie: JSESSIONID (expires when session ends)
 *  "Remember Me" implementation: hash-based toek (cookies only) and persistent token (cookies and database)
 *  remember-me cookie: expires after 14 days (default)
 *  Spring Security Filter intercepts requests to process authentication 
 */
@Configuration
public class SecurityConfig {

    /**
     * Add ignoring for encryption and decryption paths
     * Test using postman or any other client
     */
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        
        /**
         * Add ignoring for encryption and decryption paths
         * 
         * encrypt/decrypt endpoints
         * To use config server for encryption and decryption and to reach them without authentication for simplicity
         */
        return (web) -> web.ignoring()
                            .antMatchers("/actuator/**")
                            .antMatchers("/encrypt/**")
                            .antMatchers("/decrypt/**");
    }    
    
}
