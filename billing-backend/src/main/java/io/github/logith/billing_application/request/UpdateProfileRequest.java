package io.github.logith.billing_application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "Name is required")
        String name,

        @Size(min = 8, message = "Password must be at least 8 characters")
        String password   // optional — null means "don't change password"
) {}
