package io.github.logith.billing_application.response;

import io.github.logith.billing_application.entity.Plan;
import io.github.logith.billing_application.entity.enums.BillingCycle;

public record PlanResponse(
    Long id,
    String name,
    Double price,
    BillingCycle billingCycle
){
    public static PlanResponse fromEntity(Plan plan){
        return new PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getPrice().doubleValue(),
                plan.getBillingCycle()
        );
    }
}
