package io.github.logith.billing_application.request;

import io.github.logith.billing_application.entity.enums.BillingCycle;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlanRequest (

    @NotBlank(message = "Plan name is required")
    String name,

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message="Price must be greater than 0")
    Double price,

    @NotNull(message = "Billing Cycle is Required")
    BillingCycle billingCycle
){}
