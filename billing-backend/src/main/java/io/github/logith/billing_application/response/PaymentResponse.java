package io.github.logith.billing_application.response;

import io.github.logith.billing_application.entity.Payment;
import io.github.logith.billing_application.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long invoiceId,
        BigDecimal amount,
        PaymentStatus status,
        String transactionId,
        String paymentMethod,
        String failureReason,
        LocalDateTime paymentDate
) {
    public static PaymentResponse fromEntity(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getInvoice().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getPaymentMethod(),
                payment.getFailureReason(),
                payment.getPaymentDate()
        );
    }
}
