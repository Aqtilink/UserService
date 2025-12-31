package com.aqtilink.user_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Validates X-Service-API-Key header for inter-service communication.
 * Allows requests with either:
 * 1. Valid X-Service-API-Key header (service-to-service)
 * 2. Valid JWT token in Authorization header (user-to-service)
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
            String authHeader = request.getHeader("Authorization");
            
            // Allow if either:
            // 1. Valid API key is provided (service-to-service)
            // 2. JWT token is provided (user-to-service, will be validated by OAuth2)
            if (providedKey != null && providedKey.equals(expectedApiKey)) {
                // Valid API key - allow service-to-service call
                filterChain.doFilter(request, response);
                return;
            }
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // JWT token provided - let the OAuth2 filter handle validation
                filterChain.doFilter(request, response);
                return;
            }
            
            // Neither valid API key nor JWT token - reject
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing authentication (API Key or JWT)");
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean isServiceEndpoint(String path) {
        // Service endpoints that can be called by either services or authenticated users
        return path.contains("/api/v1/users/") && (path.contains("/friends") || path.endsWith("/email"));
    }
}
    }
}
