package io.github.logith.billing_application.service;

import io.github.logith.billing_application.request.LoginRequest;
import io.github.logith.billing_application.request.RegisterRequest;
import io.github.logith.billing_application.response.AuthResponse;
import io.github.logith.billing_application.entity.User;
import io.github.logith.billing_application.entity.enums.UserRole;
import io.github.logith.billing_application.repository.UserRepository;
import io.github.logith.billing_application.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        // 1. Check duplicate email
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // 2. Build user entity
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); // ✅ never store plain text
        user.setRole(UserRole.ROLE_USER);

        // 3. Save to DB
        User saved = userRepository.save(user);

        // 4. Generate and return token
        String token = jwtService.generateToken(saved.getId(), saved.getRole().name());
        return new AuthResponse(token, saved.getName(), saved.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Find user
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        // 2. Verify password
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials"); // same msg — don't leak info
        }

        // 3. Generate and return token
        String token = jwtService.generateToken(user.getId(), user.getRole().name());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }
}