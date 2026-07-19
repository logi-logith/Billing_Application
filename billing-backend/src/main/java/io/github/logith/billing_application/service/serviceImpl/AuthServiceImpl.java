package io.github.logith.billing_application.service.serviceImpl;

import io.github.logith.billing_application.entity.User;
import io.github.logith.billing_application.entity.enums.UserRole;
import io.github.logith.billing_application.exception.BusinessException;
import io.github.logith.billing_application.repository.UserRepository;
import io.github.logith.billing_application.request.AdminRegisterRequest;
import io.github.logith.billing_application.request.LoginRequest;
import io.github.logith.billing_application.request.RegisterRequest;
import io.github.logith.billing_application.response.AuthResponse;
import io.github.logith.billing_application.security.JwtService;
import io.github.logith.billing_application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${admin.secret-key}")
    private String adminSecretKey;

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Register attempt for email: {}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            log.warn("Registration failed — email already registered: {}", request.email());
            throw new BusinessException("Email already registered");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.ROLE_USER);

        User saved = userRepository.save(user);
        log.info("New user registered — id: {}, email: {}", saved.getId(), saved.getEmail());

        String token = jwtService.generateToken(saved.getId(), saved.getRole().name());
        return new AuthResponse(token, saved.getName(), saved.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Login failed — email not found: {}", request.email());
                    return new BadCredentialsException("Invalid credentials");
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Login failed — wrong password for email: {}", request.email());
            throw new BadCredentialsException("Invalid credentials");
        }

        log.info("Login successful — userId: {}, role: {}", user.getId(), user.getRole());
        String token = jwtService.generateToken(user.getId(), user.getRole().name());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }

    @Override
    public AuthResponse registerAdmin(AdminRegisterRequest request) {
        log.info("Admin register attempt for email: {}", request.emailId());

        if (!adminSecretKey.equals(request.adminSecretKey())) {
            log.warn("Admin registration rejected — invalid secret key for email: {}", request.emailId());
            throw new AccessDeniedException("Invalid admin secret key");
        }

        if (userRepository.existsByEmail(request.emailId())) {
            log.warn("Admin registration failed — email already registered: {}", request.emailId());
            throw new BusinessException("Email already registered");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.emailId());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.ROLE_ADMIN);

        User saved = userRepository.save(user);
        log.info("New ADMIN registered — id: {}, email: {}", saved.getId(), saved.getEmail());

        String token = jwtService.generateToken(saved.getId(), saved.getRole().name());
        return new AuthResponse(token, saved.getName(), saved.getRole().name());
    }
}
