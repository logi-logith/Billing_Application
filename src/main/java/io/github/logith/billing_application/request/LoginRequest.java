package io.github.logith.billing_application.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
    @Email @NotBlank String email,
    @NotBlank
    String password
) {}
