package io.github.logith.billing_application.service.serviceImpl;

import io.github.logith.billing_application.entity.Plan;
import io.github.logith.billing_application.exception.ResourceNotFoundException;
import io.github.logith.billing_application.repository.PlanRepository;
import io.github.logith.billing_application.request.PlanRequest;
import io.github.logith.billing_application.response.PlanResponse;
import io.github.logith.billing_application.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private static final Logger log = LoggerFactory.getLogger(PlanServiceImpl.class);

    private final PlanRepository planRepository;

    @Override
    public List<PlanResponse> getAllActivePlans() {
        log.debug("Fetching all active plans");
        List<PlanResponse> plans = planRepository.findByActiveTrue()
                .stream()
                .map(PlanResponse::fromEntity)
                .toList();
        log.debug("Found {} active plans", plans.size());
        return plans;
    }

    @Override
    public PlanResponse getPlanById(Long id) {
        log.debug("Fetching plan by id: {}", id);
        // FIX: was throwing raw RuntimeException — now uses ResourceNotFoundException (→ 404)
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
        return PlanResponse.fromEntity(plan);
    }

    @Override
    public PlanResponse createPlan(PlanRequest planRequest) {
        log.info("Creating new plan: name={}, price={}, cycle={}",
                planRequest.name(), planRequest.price(), planRequest.billingCycle());
        Plan plan = new Plan();
        plan.setName(planRequest.name());
        plan.setPrice(BigDecimal.valueOf(planRequest.price()));
        plan.setBillingCycle(planRequest.billingCycle());
        plan.setActive(true);
        Plan saved = planRepository.save(plan);
        log.info("Plan created — id: {}, name: {}", saved.getId(), saved.getName());
        return PlanResponse.fromEntity(saved);
    }

    @Override
    public PlanResponse deactivePlan(Long id) {
        log.info("Deactivating plan id: {}", id);
        // FIX: was throwing raw RuntimeException
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + id));
        plan.setActive(false);
        Plan saved = planRepository.save(plan);
        log.info("Plan deactivated — id: {}", saved.getId());
        return PlanResponse.fromEntity(saved);
    }
}
