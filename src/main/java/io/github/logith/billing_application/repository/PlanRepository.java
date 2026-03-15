package io.github.logith.billing_application.repository;

import io.github.logith.billing_application.entity.Plan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository {
    List<Plan> findByActiveTrue();
}
