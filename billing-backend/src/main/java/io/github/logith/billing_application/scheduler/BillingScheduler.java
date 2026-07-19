package io.github.logith.billing_application.scheduler;

import io.github.logith.billing_application.entity.Invoice;
import io.github.logith.billing_application.entity.Subscription;
import io.github.logith.billing_application.entity.enums.SubscriptionStatus;
import io.github.logith.billing_application.repository.SubscriptionRepository;
import io.github.logith.billing_application.response.InvoiceResponse;
import io.github.logith.billing_application.response.PaymentResponse;
import io.github.logith.billing_application.service.InvoiceService;
import io.github.logith.billing_application.service.PaymentService;
import io.github.logith.billing_application.repository.InvoiceRepository;
import io.github.logith.billing_application.entity.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Runs daily billing logic:
 *  1. Finds all ACTIVE subscriptions whose nextBillingDate <= today
 *  2. Generates an invoice for each
 *  3. Attempts payment
 *  4. On success: advances nextBillingDate to next cycle
 *  5. On failure: marks subscription PAYMENT_FAILED
 */
@Component
@RequiredArgsConstructor
public class BillingScheduler {

    private static final Logger log = LoggerFactory.getLogger(BillingScheduler.class);

    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;

    /**
     * Runs at 2:00 AM every day.
     * Cron: second minute hour day month weekday
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void runDailyBillingJob() {
        LocalDate today = LocalDate.now();
        log.info("=== Billing scheduler started for date: {} ===", today);

        List<Subscription> dueSubscriptions =
                subscriptionRepository.findSubscriptionsDueForBilling(today);

        log.info("Found {} subscriptions due for billing", dueSubscriptions.size());

        int successCount = 0;
        int failureCount = 0;

        for (Subscription subscription : dueSubscriptions) {
            try {
                log.info("Processing billing for subscriptionId: {}, userId: {}, plan: {}",
                        subscription.getId(),
                        subscription.getUser().getId(),
                        subscription.getPlan().getName());

                // 1. Generate invoice
                InvoiceResponse invoiceResponse = invoiceService.generateInvoice(subscription);

                // 2. Load full invoice entity for payment processing
                Invoice invoice = invoiceRepository.findById(invoiceResponse.id())
                        .orElseThrow(() -> new IllegalStateException(
                                "Invoice not found after save: " + invoiceResponse.id()));

                // 3. Attempt payment
                PaymentResponse paymentResponse = paymentService.processPayment(invoice);

                if (paymentResponse.status() == PaymentStatus.SUCCESS) {
                    // 4a. Advance nextBillingDate to next cycle
                    LocalDate nextBillingDate = switch (subscription.getPlan().getBillingCycle()) {
                        case MONTHLY   -> today.plusMonths(1);
                        case QUARTERLY -> today.plusMonths(3);
                        case YEARLY    -> today.plusYears(1);
                    };
                    subscription.setNextBillingDate(nextBillingDate);
                    subscriptionRepository.save(subscription);

                    log.info("Billing SUCCESS — subscriptionId: {}, txnId: {}, nextBillingDate: {}",
                            subscription.getId(), paymentResponse.transactionId(), nextBillingDate);
                    successCount++;
                } else {
                    // 4b. Mark subscription as PAYMENT_FAILED
                    subscription.setSubscriptionStatus(SubscriptionStatus.PAYMENT_FAILED);
                    subscriptionRepository.save(subscription);

                    log.warn("Billing FAILED — subscriptionId: {}, userId: {}, reason: {}",
                            subscription.getId(),
                            subscription.getUser().getId(),
                            paymentResponse.failureReason());
                    failureCount++;
                }

            } catch (Exception e) {
                // Don't let one subscription failure kill the entire job
                log.error("Unexpected error billing subscriptionId: {} — skipping. Error: {}",
                        subscription.getId(), e.getMessage(), e);
                failureCount++;
            }
        }

        log.info("=== Billing job complete — success: {}, failed: {}, total: {} ===",
                successCount, failureCount, dueSubscriptions.size());
    }
}
