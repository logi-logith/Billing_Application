package io.github.logith.billing_application.repository;

import io.github.logith.billing_application.entity.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository {
    List<Payment> findByInvoiceId(Long invoiceId);

    // Find by transaction ID (from payment gateway)
    Optional<Payment> findByTransactionId(String transactionId);

    // Payment history for a user
    @Query("""
        SELECT p FROM Payment p
        JOIN FETCH p.invoice i
        JOIN FETCH i.subscription s
        WHERE s.user.id = :userId
        ORDER BY p.paymentDate DESC
    """)
    List<Payment> findPaymentHistoryByUserId(@Param("userId") Long userId);
}

