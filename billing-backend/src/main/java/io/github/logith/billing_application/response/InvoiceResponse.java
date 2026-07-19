package io.github.logith.billing_application.response;

import io.github.logith.billing_application.entity.Invoice;
import io.github.logith.billing_application.entity.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InvoiceResponse(
        Long id,
        Long subscriptionId,
        Long userId,
        String userEmail,
        BigDecimal amount,
        LocalDate dueDate,
        LocalDateTime issuedAt,
        InvoiceStatus status
) {
    public static InvoiceResponse fromEntity(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getSubscription().getId(),
                invoice.getSubscription().getUser().getId(),
                invoice.getSubscription().getUser().getEmail(),
                invoice.getAmount(),
                invoice.getDueDate(),
                invoice.getIssuedAt(),
                invoice.getStatus()
        );
    }
}
