package io.github.logith.billing_application.Entity;

import io.github.logith.billing_application.Entity.Enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private LocalDate startDate;

    private LocalDate nextBillingDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
}
