package io.github.logith.billing_application.service;

import io.github.logith.billing_application.entity.Invoice;
import io.github.logith.billing_application.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(Invoice invoice);
    List<PaymentResponse> getPaymentHistory(Long userId);
}
