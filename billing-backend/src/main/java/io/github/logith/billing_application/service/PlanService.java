package io.github.logith.billing_application.service;

import io.github.logith.billing_application.entity.Plan;
import io.github.logith.billing_application.request.PlanRequest;
import io.github.logith.billing_application.response.PlanResponse;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getAllActivePlans();
    PlanResponse getPlanById(Long id);
    PlanResponse createPlan(PlanRequest planRequest);
    PlanResponse deactivePlan(Long id);
}
