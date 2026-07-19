package io.github.logith.billing_application.repository;

import io.github.logith.billing_application.entity.Subscription;
import io.github.logith.billing_application.entity.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId(Long userId);

    // FIX: corrected field name from "status" → "subscriptionStatus" to match entity field
    Optional<Subscription> findByUserIdAndSubscriptionStatus(Long userId, SubscriptionStatus subscriptionStatus);

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
        WHERE s.subscriptionStatus = 'ACTIVE'
        AND s.nextBillingDate <= :today
    """)
    List<Subscription> findSubscriptionsDueForBilling(@Param("today") LocalDate today);
}
