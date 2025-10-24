//package com.stacs.cs5031.p3.server.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()                      // Disable CSRF (important for POST requests)
//            .authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll()          // Allow everything without authentication
//            );
//        return http.build();
//    }
//}