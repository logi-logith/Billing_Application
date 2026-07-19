package io.github.logith.billing_application.service;

import io.github.logith.billing_application.entity.Subscription;
import io.github.logith.billing_application.response.InvoiceResponse;

import java.util.List;

public interface InvoiceService {
    InvoiceResponse generateInvoice(Subscription subscription);
    List<InvoiceResponse> getInvoicesByUser(Long userId);
    List<InvoiceResponse> getInvoicesBySubscription(Long subscriptionId);
}
