package io.github.logith.billing_application.repository;

import io.github.logith.billing_application.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {
    List<Plan> findByActiveTrue();
}
