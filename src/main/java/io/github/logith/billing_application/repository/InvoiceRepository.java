package io.github.logith.billing_application.repository;

import io.github.logith.billing_application.entity.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository {

    List<Invoice> findBySubscriptionId(Long Subscription);

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
