package io.github.logith.billing_application.controller;

import io.github.logith.billing_application.response.InvoiceResponse;
import io.github.logith.billing_application.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    private final InvoiceService invoiceService;

    // GET /api/invoices — get all invoices for the logged-in user
    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getMyInvoices(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        log.debug("GET /api/invoices for userId: {}", userId);
        return ResponseEntity.ok(invoiceService.getInvoicesByUser(userId));
    }

    // GET /api/invoices/subscription/{id} — invoices for a specific subscription
    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<InvoiceResponse>> getBySubscription(
            @PathVariable Long subscriptionId,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        log.debug("GET /api/invoices/subscription/{} for userId: {}", subscriptionId, userId);
        return ResponseEntity.ok(invoiceService.getInvoicesBySubscription(subscriptionId));
    }
}
