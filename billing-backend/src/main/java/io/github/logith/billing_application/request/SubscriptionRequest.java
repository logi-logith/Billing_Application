package io.github.logith.billing_application.request;

import jakarta.validation.constraints.NotNull;

public record SubscriptionRequest(
        @NotNull(message = "Plan Id is required")
        Long planId
) {
}
