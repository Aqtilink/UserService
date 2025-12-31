package com.aqtilink.user_service.security;

import com.aqtilink.user_service.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Ensures a local user record exists for the authenticated Clerk user.
 */
public class ClerkUserProvisioningFilter extends OncePerRequestFilter {

    private final UserService userService;

    public ClerkUserProvisioningFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken && authentication.isAuthenticated()) {
            String clerkId = SecurityUtils.getCurrentClerkId();
            if (clerkId != null) {
                // Use claims to populate best-effort user profile when first seen
                String email = SecurityUtils.getCurrentEmail();
                String firstName = SecurityUtils.getCurrentFirstName();
                String lastName = SecurityUtils.getCurrentLastName();
                userService.getOrCreateByClerkId(clerkId, email, firstName, lastName);
            }
        }

        filterChain.doFilter(request, response);
    }
}
