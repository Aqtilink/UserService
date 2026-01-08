package com.aqtilink.user_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Filter to authenticate service-to-service requests using an API key

@Component
public class ServiceApiKeyFilter extends OncePerRequestFilter {

    @Value("${service.api-key:dev-secret-key-change-in-production}")
    private String expectedApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        if (isServiceEndpoint(path)) {
            String providedKey = request.getHeader("X-Service-API-Key");
            String authHeader = request.getHeader("Authorization");
            
            if (providedKey != null && providedKey.equals(expectedApiKey)) {
                var auth = new PreAuthenticatedAuthenticationToken(
                        "service", null, List.of(new SimpleGrantedAuthority("ROLE_SERVICE"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
                return;
            }
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing authentication (API Key or JWT)");
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean isServiceEndpoint(String path) {
        return path.startsWith("/api/v1/users/");
    }
}
