package com.aqtilink.user_service.security;

import com.aqtilink.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for User Service.
 * Requires JWT authentication for user-facing endpoints.
 * Requires API key for inter-service communication endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ServiceApiKeyFilter apiKeyFilter, ClerkUserProvisioningFilter provisioningFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/users/*/friends").permitAll() // Handled by API key filter
                .requestMatchers(HttpMethod.GET, "/api/v1/users/*/email").permitAll() // Handled by API key filter
                .requestMatchers("/api/v1/friend-requests/**").authenticated()
                .requestMatchers("/api/v1/users/**").authenticated()
                .anyRequest().authenticated()
            )
            // Add API key filter before authorization
            .addFilterBefore(apiKeyFilter, AuthorizationFilter.class)
            // Run after authorization so the SecurityContext already contains the JWT auth
            .addFilterAfter(provisioningFilter, AuthorizationFilter.class);

        // Only configure OAuth2 resource server if JWK URI is provided
        if (jwkSetUri != null && !jwkSetUri.isEmpty()) {
            http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );
        }

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        if (jwkSetUri == null || jwkSetUri.isEmpty()) {
            throw new IllegalStateException("JWK Set URI must be configured");
        }
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public ClerkUserProvisioningFilter clerkUserProvisioningFilter(UserService userService) {
        return new ClerkUserProvisioningFilter(userService);
    }
}
