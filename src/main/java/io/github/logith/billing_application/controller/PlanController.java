package io.github.logith.billing_application.controller;

import io.github.logith.billing_application.entity.Plan;
import io.github.logith.billing_application.request.PlanRequest;
import io.github.logith.billing_application.response.PlanResponse;
import io.github.logith.billing_application.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getAllPlans(){
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable Long id){
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlanResponse> createPlan(@RequestBody PlanRequest planRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.createPlan(planRequest));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlanResponse> deactivatePlan(@PathVariable Long id){
        return ResponseEntity.ok(planService.deactivePlan(id));
    }
}
