package io.github.logith.billing_application.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extract Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. If no token → skip (let Security handle as anonymous)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // 4. Validate token
        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Extract user info from token
        Long userId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);

        // 6. Build authentication object
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userId,                                          // principal
                        null,                                            // credentials (not needed)
                        List.of(new SimpleGrantedAuthority(role))       // authorities
                );

        // 7. Set in SecurityContext — request is now "authenticated"
        SecurityContextHolder.getContext().getAuthentication();

        // 8. Continue filter chain
        filterChain.doFilter(request, response);

//        if (!jwtService.isTokenValid(token)) {
//            System.out.println("❌ Token is INVALID");
//            filterChain.doFilter(request, response);
//            return;
//        }

        System.out.println("Token valid, userId: " + jwtService.extractUserId(token));
        System.out.println("Role: " + jwtService.extractRole(token));
        System.out.println("Auth set in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());
    }
}