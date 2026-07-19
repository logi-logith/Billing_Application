package io.github.logith.billing_application.controller;

import io.github.logith.billing_application.request.PlanRequest;
import io.github.logith.billing_application.response.PlanResponse;
import io.github.logith.billing_application.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private static final Logger log = LoggerFactory.getLogger(PlanController.class);

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        log.debug("GET /api/plans");
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable Long id) {
        log.debug("GET /api/plans/{}", id);
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlanResponse> createPlan(@Valid @RequestBody PlanRequest planRequest) {
        log.info("POST /api/plans — creating plan: {}", planRequest.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.createPlan(planRequest));
    }

    // FIX: was @DeleteMapping with no path variable — /{id} was missing, id was never bound
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlanResponse> deactivatePlan(@PathVariable Long id) {
        log.info("DELETE /api/plans/{} — deactivating plan", id);
        return ResponseEntity.ok(planService.deactivePlan(id));
    }
}
