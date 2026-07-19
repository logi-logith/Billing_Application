package io.github.logith.billing_application.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        log.debug("JWT filter processing request: {} {}", request.getMethod(), path);

        // 1. Extract Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. If no token → skip (public endpoints or unauthenticated)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token found for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // 4. Validate token
        if (!jwtService.isTokenValid(token)) {
            log.warn("Invalid or expired JWT token for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Extract user info from token
        Long userId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);
        log.debug("JWT valid — userId: {}, role: {}", userId, role);

        // 6. Build authentication object
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userId,                                         // principal (accessible via authentication.getPrincipal())
                        null,                                           // credentials (not needed post-auth)
                        List.of(new SimpleGrantedAuthority(role))      // authorities for @PreAuthorize
                );

        // 7. FIX: was calling getAuthentication() (read) instead of setAuthentication() (write)
        //    This was the root cause of all 403 errors on authenticated endpoints
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("SecurityContext populated for userId: {}", userId);

        // 8. Continue filter chain
        filterChain.doFilter(request, response);
    }
}
