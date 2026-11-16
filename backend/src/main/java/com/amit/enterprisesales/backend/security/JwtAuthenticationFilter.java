
package com.amit.enterprisesales.backend.security;

import com.amit.enterprisesales.backend.entity.User;
import com.amit.enterprisesales.backend.entity.UserRole;
import com.amit.enterprisesales.backend.repository.UserRepository;

import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        // Step 1: Validate JWT signature and expiration.
        Claims claims;

        try {
            claims = jwtService.extractAllClaims(token);
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or expired authentication token"
            );
            return;
        }

        String userId = claims.getSubject();
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        if (userId == null || email == null || role == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid authentication token"
            );
            return;
        }

        // Step 2: Validate user ID format.
        UUID tokenUserId;

        try {
            tokenUserId = UUID.fromString(userId);
        } catch (IllegalArgumentException exception) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid user ID in authentication token"
            );
            return;
        }

        // Step 3: Validate role against the allowed enum.
        UserRole tokenRole;

        try {
            tokenRole = UserRole.valueOf(role);
        } catch (IllegalArgumentException exception) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid role in authentication token"
            );
            return;
        }

        // Step 4: Retrieve current user from PostgreSQL.
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        if (user == null ||
                !user.isActive() ||
                !user.getId().equals(tokenUserId)) {

            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User account is not found or inactive"
            );
            return;
        }

        // Step 5: Ensure the JWT role matches the current database role.
        if (user.getUserRole() != tokenRole) {
            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User role has changed. Please log in again."
            );
            return;
        }

        // Step 6: Set authenticated user using current database role.
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + user.getUserRole().name()
                                )
                        )
                );

        authentication.setDetails(user.getId());

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
