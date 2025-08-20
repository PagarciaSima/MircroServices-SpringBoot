package com.pgs.microservices.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
	
	private final String [] freeResourceUrls = {
			
		"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/api-docs/**", "/aggregate/**",
		"/aggregate/product-service/v3/api-docs/**", "/actuator/prometheus"
	};

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
	        
    		.authorizeHttpRequests(authorize -> 
	    	    authorize.requestMatchers(freeResourceUrls).permitAll()
	    	    // Require authentication for any request to the application
	    	     .anyRequest().authenticated()
	    	)
    		 .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        // Configure the application as an OAuth2 Resource Server using JWT tokens
	        .oauth2ResourceServer(oauth2 -> 
	            oauth2.jwt(Customizer.withDefaults()) // Use default JWT decoder and validation settings
	        )
	        .build(); // Build and return the SecurityFilterChain bean
	}
	
	 @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
