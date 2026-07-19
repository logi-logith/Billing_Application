package io.github.logith.billing_application.repository;

import io.github.logith.billing_application.entity.Invoice;
import io.github.logith.billing_application.entity.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// FIX: was missing extends JpaRepository — no CRUD methods available before
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findBySubscriptionId(Long subscriptionId);

    List<Invoice> findBySubscriptionIdAndStatus(Long subscriptionId, InvoiceStatus status);

    @Query("""
            SELECT i FROM Invoice i
                    JOIN FETCH i.subscription s
                    JOIN FETCH s.user u
                    WHERE u.id = :userId
                    ORDER BY i.issuedAt DESC
            """)
    List<Invoice> findAllByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT i FROM Invoice i
        WHERE i.status = 'PENDING'
        AND i.dueDate < CURRENT_DATE
    """)
    List<Invoice> findOverdueInvoices();
}
