package io.github.logith.billing_application.controller;

import io.github.logith.billing_application.response.PaymentResponse;
import io.github.logith.billing_application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    // GET /api/payments/history — payment history for logged-in user
    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        log.debug("GET /api/payments/history for userId: {}", userId);
        return ResponseEntity.ok(paymentService.getPaymentHistory(userId));
    }
}
