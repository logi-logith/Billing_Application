package io.github.logith.billing_application.repository;

import io.github.logith.billing_application.entity.enums.SubscriptionStatus;
import io.github.logith.billing_application.entity.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository {
    List<Subscription> findByUserId(Long userId);

    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    @Query("""
        SELECT s FROM Subscription s
                JOIN FETCH s.user
                JOIN FETCH s.plan
                WHERE s.id = :id
    """)
    Optional<Subscription> findByIdWithDetails(@Param("id") Long id);

    @Query("""
        SELECT s FROM Subscription s
        JOIN FETCH s.user
        JOIN FETCH s.plan
        WHERE s.status = 'ACTIVE'
        AND s.nextBillingDate <= :today
    """)
    List<Subscription> findSubscriptionDueForBilling(@Param("today") LocalDate today);
}
