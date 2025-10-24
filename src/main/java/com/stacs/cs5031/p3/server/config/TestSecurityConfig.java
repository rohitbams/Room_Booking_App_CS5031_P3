package com.stacs.cs5031.p3.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for testing purposes.
 * This configuration disables CSRF protection and permits all requests without authentication.
 * It should only be used in test environments and not in production.
 */
@Configuration
@EnableWebSecurity
public class TestSecurityConfig {
    
    /**
     * Configures the security filter chain for the application.
     * This implementation:
     * 1. Disables CSRF protection
     * 2. Allows all requests without authentication
     *
     * @param http The HttpSecurity object to configure
     * @return The configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll());
        return http.build();
    }
}