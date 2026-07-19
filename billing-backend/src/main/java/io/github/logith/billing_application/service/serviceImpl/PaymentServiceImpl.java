package io.github.logith.billing_application.service.serviceImpl;

import io.github.logith.billing_application.entity.Invoice;
import io.github.logith.billing_application.entity.Payment;
import io.github.logith.billing_application.entity.enums.InvoiceStatus;
import io.github.logith.billing_application.entity.enums.PaymentStatus;
import io.github.logith.billing_application.repository.InvoiceRepository;
import io.github.logith.billing_application.repository.PaymentRepository;
import io.github.logith.billing_application.response.PaymentResponse;
import io.github.logith.billing_application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    /**
     * Simulates payment processing for a PENDING invoice.
     * In production, this would integrate with a payment gateway (Stripe, Razorpay, etc.).
     * Currently simulates a 90% success rate for testing the billing scheduler.
     */
    @Override
    @Transactional
    public PaymentResponse processPayment(Invoice invoice) {
        log.info("Processing payment for invoiceId: {}, amount: {}", invoice.getId(), invoice.getAmount());

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(invoice.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod("SYSTEM_AUTO");  // auto-charge; real impl would use saved card/UPI token

        // Simulate payment gateway call (replace with real gateway integration)
        boolean paymentSuccess = simulateGatewayCall();

        if (paymentSuccess) {
            String txnId = "TXN-" + UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 16);
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(txnId);

            invoice.setStatus(InvoiceStatus.PAID);
            log.info("Payment SUCCESS — invoiceId: {}, transactionId: {}", invoice.getId(), txnId);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Simulated gateway decline");

            invoice.setStatus(InvoiceStatus.FAILED);
            log.warn("Payment FAILED — invoiceId: {}, reason: simulated gateway decline", invoice.getId());
        }

        invoiceRepository.save(invoice);
        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(saved);
    }

    @Override
    public List<PaymentResponse> getPaymentHistory(Long userId) {
        log.debug("Fetching payment history for userId: {}", userId);
        return paymentRepository.findPaymentHistoryByUserId(userId)
                .stream()
                .map(PaymentResponse::fromEntity)
                .toList();
    }

    // Simulates a gateway with ~90% success rate.
    // Replace this method body with a real HTTP call to your payment provider.
    private boolean simulateGatewayCall() {
        return Math.random() < 0.9;
    }
}
