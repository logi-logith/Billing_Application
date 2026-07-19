package io.github.logith.billing_application.service;

import io.github.logith.billing_application.request.AdminRegisterRequest;
import io.github.logith.billing_application.request.LoginRequest;
import io.github.logith.billing_application.request.RegisterRequest;
import io.github.logith.billing_application.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse registerAdmin(AdminRegisterRequest request);
}
