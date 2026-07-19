package io.github.logith.billing_application.response;

import io.github.logith.billing_application.entity.Subscription;
import io.github.logith.billing_application.entity.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long id,
        Long userId,
        String userEmail,
        Long planId,
        String planName,
        Double planPrice,
        String billingCycle,
        SubscriptionStatus status,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate nextBillingDate,
        LocalDateTime createdAt
) {
    public static SubscriptionResponse fromEntity(Subscription s){
        return new SubscriptionResponse(
                s.getId(),
                s.getUser().getId(),
                s.getUser().getEmail(),
                s.getPlan().getId(),
                s.getPlan().getName(),
                s.getPlan().getPrice().doubleValue(),
                s.getPlan().getBillingCycle().name(),
                s.getSubscriptionStatus(),
                s.getStartDate(),
                s.getEndDate(),
                s.getNextBillingDate(),
                s.getCreatedAt()
        );
    }
}
