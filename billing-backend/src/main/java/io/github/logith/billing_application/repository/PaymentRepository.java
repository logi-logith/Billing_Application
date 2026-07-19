package io.github.logith.billing_application.repository;

import io.github.logith.billing_application.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// FIX: was missing extends JpaRepository — no CRUD methods available before
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceId(Long invoiceId);

    Optional<Payment> findByTransactionId(String transactionId);

    @Query("""
        SELECT p FROM Payment p
        JOIN FETCH p.invoice i
        JOIN FETCH i.subscription s
        WHERE s.user.id = :userId
        ORDER BY p.paymentDate DESC
    """)
    List<Payment> findPaymentHistoryByUserId(@Param("userId") Long userId);
}
