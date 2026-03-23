package io.github.logith.billing_application.service.serviceImpl;

import io.github.logith.billing_application.entity.Plan;
import io.github.logith.billing_application.repository.PlanRepository;
import io.github.logith.billing_application.request.PlanRequest;
import io.github.logith.billing_application.response.PlanResponse;
import io.github.logith.billing_application.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    @Override
    public List<PlanResponse> getAllActivePlans() {         //Get All Active Plans
        return planRepository.findByActiveTrue()
                .stream()
                .map(PlanResponse::fromEntity)
                .toList();
    }

    @Override
    public PlanResponse getPlanById(Long id) {       //Get plan by id
        Plan plan = planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));
        return PlanResponse.fromEntity(plan);
    }

    @Override
    public PlanResponse createPlan(PlanRequest planRequest) {      //Admin - create plan
        Plan plan = new Plan();
        plan.setName(planRequest.name());
        plan.setPrice(BigDecimal.valueOf(planRequest.price()));
        plan.setBillingCycle(planRequest.billingCycle());
        plan.setActive(true);
        return PlanResponse.fromEntity(planRepository.save(plan));
    }

    @Override
    public PlanResponse deactivePlan(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan not found: " + id));
        plan.setActive(false);
        return PlanResponse.fromEntity(planRepository.save(plan));
    }
}
