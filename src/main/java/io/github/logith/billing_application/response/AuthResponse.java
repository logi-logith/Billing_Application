package io.github.logith.billing_application.response;

public record AuthResponse(
        String accessToken,
        String name,
        String role
) {}