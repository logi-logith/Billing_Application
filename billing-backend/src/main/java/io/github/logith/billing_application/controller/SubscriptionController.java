package io.github.logith.billing_application.controller;

import io.github.logith.billing_application.request.SubscriptionRequest;
import io.github.logith.billing_application.response.SubscriptionResponse;
import io.github.logith.billing_application.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // POST /api/subscriptions — subscribe to a plan
    @PostMapping
    public ResponseEntity<SubscriptionResponse> subscribe(
            @Valid @RequestBody SubscriptionRequest request,
            Authentication authentication) {                    // ← Spring injects this

        Long userId = (Long) authentication.getPrincipal();     // ← from JwtAuthFilter
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.subscribe(userId, request));
    }

    // GET /api/subscriptions — get my subscriptions
    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(
                subscriptionService.getMySubscriptions(userId));
    }

    // GET /api/subscriptions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(
                subscriptionService.getById(id, userId));
    }

    // PUT /api/subscriptions/{id}/cancel
    @PutMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponse> cancel(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(
                subscriptionService.cancel(id, userId));
    }

    // PUT /api/subscriptions/{id}/pause
    @PutMapping("/{id}/pause")
    public ResponseEntity<SubscriptionResponse> pause(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(
                subscriptionService.pause(id, userId));
    }

    // PUT /api/subscriptions/{id}/resume
    @PutMapping("/{id}/resume")
    public ResponseEntity<SubscriptionResponse> resume(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(
                subscriptionService.resume(id, userId));
    }
}