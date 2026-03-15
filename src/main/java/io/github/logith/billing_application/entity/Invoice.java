package io.github.logith.billing_application.entity;

import io.github.logith.billing_application.entity.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    private BigDecimal amount;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
}
