package io.github.logith.billing_application.controller;

import io.github.logith.billing_application.request.AdminRegisterRequest;
import io.github.logith.billing_application.request.LoginRequest;
import io.github.logith.billing_application.request.RegisterRequest;
import io.github.logith.billing_application.response.AuthResponse;
import io.github.logith.billing_application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request){
        return ResponseEntity.ok(authService.registerAdmin(request));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.login(request));
    }
}