package com.pgs.microservices.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	/**
	 * Configures the security filter chain for the application.
	 * 
	 * This configuration ensures that:
	 * - All HTTP requests require authentication.
	 * - The application acts as an OAuth2 Resource Server using JWT tokens for authentication.
	 * 
	 * @param httpSecurity the {@link HttpSecurity} to modify
	 * @return the configured {@link SecurityFilterChain}
	 * @throws Exception if an error occurs while building the security filter chain
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	    return httpSecurity
	        // Require authentication for any request to the application
	        .authorizeHttpRequests(authorize -> 
	            authorize.anyRequest().authenticated()
	        )
	        // Configure the application as an OAuth2 Resource Server using JWT tokens
	        .oauth2ResourceServer(oauth2 -> 
	            oauth2.jwt(Customizer.withDefaults()) // Use default JWT decoder and validation settings
	        )
	        .build(); // Build and return the SecurityFilterChain bean
	}
}
