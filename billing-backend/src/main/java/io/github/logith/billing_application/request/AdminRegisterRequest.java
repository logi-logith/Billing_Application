package io.github.logith.billing_application.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record AdminRegisterRequest(

        @NotBlank(message = "Name is required")
        String name,
        @Email
        @NotBlank(message = "Email Id is required")
        String emailId,
        @NotBlank(message = "Password is Required")
        String password,
        @NotBlank(message = "Client Secret is Required")
        String adminSecretKey
) {}
