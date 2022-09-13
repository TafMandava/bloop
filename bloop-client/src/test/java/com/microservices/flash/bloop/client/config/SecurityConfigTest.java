package com.microservices.flash.bloop.client.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityConfigTest {

    /**
     * Various encoded passwords are generated
     *     BCrypt uses random salts for directing hash values
     *
     * matches
     *     Compare the rawPassword to the encoded password
     * 
     */    
    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println(encodedPassword);

        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        assertThat(matches).isTrue();
    }

}
