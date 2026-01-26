package io.github.logith.billing_application.Entity;

import io.github.logith.billing_application.Entity.Enums.BillingCycle;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plans")
@Data
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  id;

    private String  name;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle;

    private Boolean active = true;
}
