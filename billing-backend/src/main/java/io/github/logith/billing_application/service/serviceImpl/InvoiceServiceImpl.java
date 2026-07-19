package io.github.logith.billing_application.service.serviceImpl;

import io.github.logith.billing_application.entity.Invoice;
import io.github.logith.billing_application.entity.Subscription;
import io.github.logith.billing_application.entity.enums.InvoiceStatus;
import io.github.logith.billing_application.repository.InvoiceRepository;
import io.github.logith.billing_application.response.InvoiceResponse;
import io.github.logith.billing_application.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public InvoiceResponse generateInvoice(Subscription subscription) {
        log.info("Generating invoice for subscriptionId: {}, userId: {}",
                subscription.getId(), subscription.getUser().getId());

        Invoice invoice = new Invoice();
        invoice.setSubscription(subscription);
        invoice.setAmount(subscription.getPlan().getPrice());
        invoice.setDueDate(subscription.getNextBillingDate());
        invoice.setStatus(InvoiceStatus.PENDING);

        Invoice saved = invoiceRepository.save(invoice);
        log.info("Invoice generated — id: {}, amount: {}, dueDate: {}",
                saved.getId(), saved.getAmount(), saved.getDueDate());
        return InvoiceResponse.fromEntity(saved);
    }

    @Override
    public List<InvoiceResponse> getInvoicesByUser(Long userId) {
        log.debug("Fetching invoices for userId: {}", userId);
        return invoiceRepository.findAllByUserId(userId)
                .stream()
                .map(InvoiceResponse::fromEntity)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getInvoicesBySubscription(Long subscriptionId) {
        log.debug("Fetching invoices for subscriptionId: {}", subscriptionId);
        return invoiceRepository.findBySubscriptionId(subscriptionId)
                .stream()
                .map(InvoiceResponse::fromEntity)
                .toList();
    }
}
