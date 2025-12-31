package com.aqtilink.user_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Validates X-Service-API-Key header for inter-service communication.
 * Applied to service-to-service endpoints.
 */
@Component
public class ServiceApiKeyFilter extends OncePerRequestFilter {

    @Value("${service.api-key:dev-secret-key-change-in-production}")
    private String expectedApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Check if this is a service-to-service endpoint
        String path = request.getRequestURI();
        if (isServiceEndpoint(path)) {
            String providedKey = request.getHeader("X-Service-API-Key");
            
            if (providedKey == null || !providedKey.equals(expectedApiKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or missing Service API Key");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean isServiceEndpoint(String path) {
        // Service endpoints for inter-service communication
        // These endpoints bypass JWT authentication and use API key instead
        return path.contains("/api/v1/users/") && (path.contains("/friends") || path.endsWith("/email"));
    }
}
