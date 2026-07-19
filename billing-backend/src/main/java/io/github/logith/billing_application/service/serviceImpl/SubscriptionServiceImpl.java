package io.github.logith.billing_application.service.serviceImpl;

import io.github.logith.billing_application.entity.Plan;
import io.github.logith.billing_application.entity.Subscription;
import io.github.logith.billing_application.entity.User;
import io.github.logith.billing_application.entity.enums.SubscriptionStatus;
import io.github.logith.billing_application.exception.BusinessException;
import io.github.logith.billing_application.exception.ResourceNotFoundException;
import io.github.logith.billing_application.exception.UnauthorizedException;
import io.github.logith.billing_application.repository.PlanRepository;
import io.github.logith.billing_application.repository.SubscriptionRepository;
import io.github.logith.billing_application.repository.UserRepository;
import io.github.logith.billing_application.request.SubscriptionRequest;
import io.github.logith.billing_application.response.SubscriptionResponse;
import io.github.logith.billing_application.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @Override
    @Transactional
    public SubscriptionResponse subscribe(Long userId, SubscriptionRequest request) {
        log.info("Subscribe request — userId: {}, planId: {}", userId, request.planId());

        // 1. Load user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // 2. Check for existing active subscription
        // FIX: updated method name to match corrected repository method
        subscriptionRepository.findByUserIdAndSubscriptionStatus(userId, SubscriptionStatus.ACTIVE)
                .ifPresent(s -> {
                    log.warn("Subscribe rejected — userId: {} already has active subscription id: {}", userId, s.getId());
                    throw new BusinessException("User already has an active subscription");
                });

        // 3. Load plan
        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + request.planId()));

        // 4. Verify plan is active
        if (!plan.getActive()) {
            log.warn("Subscribe rejected — plan id: {} is inactive", plan.getId());
            throw new BusinessException("Plan is no longer active");
        }

        // 5. Calculate next billing date based on billing cycle
        LocalDate today = LocalDate.now();
        LocalDate nextBillingDate = switch (plan.getBillingCycle()) {
            case MONTHLY   -> today.plusMonths(1);
            case QUARTERLY -> today.plusMonths(3);
            case YEARLY    -> today.plusYears(1);
        };
        log.debug("Next billing date for plan cycle {}: {}", plan.getBillingCycle(), nextBillingDate);

        // 6. Build and save subscription
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(today);
        subscription.setNextBillingDate(nextBillingDate);

        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Subscription created — id: {}, userId: {}, planId: {}", saved.getId(), userId, plan.getId());
        return SubscriptionResponse.fromEntity(saved);
    }

    @Override
    public SubscriptionResponse getById(Long subscriptionId, Long userId) {
        log.debug("Fetching subscription id: {} for userId: {}", subscriptionId, userId);
        Subscription subscription = subscriptionRepository.findByIdWithDetails(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + subscriptionId));
        if (!subscription.getUser().getId().equals(userId)) {
            log.warn("Access denied — userId: {} attempted to view subscription id: {} owned by userId: {}",
                    userId, subscriptionId, subscription.getUser().getId());
            throw new UnauthorizedException("You don't have access to this subscription");
        }
        return SubscriptionResponse.fromEntity(subscription);
    }

    @Override
    public List<SubscriptionResponse> getMySubscriptions(Long userId) {
        log.debug("Fetching all subscriptions for userId: {}", userId);
        List<SubscriptionResponse> result = subscriptionRepository.findByUserId(userId)
                .stream()
                .map(SubscriptionResponse::fromEntity)
                .toList();
        log.debug("Found {} subscriptions for userId: {}", result.size(), userId);
        return result;
    }

    @Override
    @Transactional
    public SubscriptionResponse cancel(Long subscriptionId, Long userId) {
        log.info("Cancel request — subscriptionId: {}, userId: {}", subscriptionId, userId);
        Subscription subscription = getAndVerifyOwnership(subscriptionId, userId);

        if (subscription.getSubscriptionStatus() == SubscriptionStatus.CANCELLED) {
            log.warn("Cancel rejected — subscription id: {} is already CANCELLED", subscriptionId);
            throw new BusinessException("Subscription is already cancelled");
        }

        subscription.setSubscriptionStatus(SubscriptionStatus.CANCELLED);
        subscription.setEndDate(LocalDate.now());

        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Subscription cancelled — id: {}", saved.getId());
        return SubscriptionResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public SubscriptionResponse pause(Long subscriptionId, Long userId) {
        log.info("Pause request — subscriptionId: {}, userId: {}", subscriptionId, userId);
        Subscription subscription = getAndVerifyOwnership(subscriptionId, userId);

        if (subscription.getSubscriptionStatus() != SubscriptionStatus.ACTIVE) {
            log.warn("Pause rejected — subscription id: {} is in status: {}",
                    subscriptionId, subscription.getSubscriptionStatus());
            throw new BusinessException("Only ACTIVE subscriptions can be paused");
        }

        subscription.setSubscriptionStatus(SubscriptionStatus.PAUSED);
        // nextBillingDate is intentionally frozen while paused

        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Subscription paused — id: {}", saved.getId());
        return SubscriptionResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public SubscriptionResponse resume(Long subscriptionId, Long userId) {
        log.info("Resume request — subscriptionId: {}, userId: {}", subscriptionId, userId);
        Subscription subscription = getAndVerifyOwnership(subscriptionId, userId);

        if (subscription.getSubscriptionStatus() != SubscriptionStatus.PAUSED) {
            log.warn("Resume rejected — subscription id: {} is in status: {}",
                    subscriptionId, subscription.getSubscriptionStatus());
            throw new BusinessException("Only PAUSED subscriptions can be resumed");
        }

        // Recalculate next billing from today (fresh cycle on resume)
        LocalDate nextBillingDate = switch (subscription.getPlan().getBillingCycle()) {
            case MONTHLY   -> LocalDate.now().plusMonths(1);
            case QUARTERLY -> LocalDate.now().plusMonths(3);
            case YEARLY    -> LocalDate.now().plusYears(1);
        };

        subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        subscription.setNextBillingDate(nextBillingDate);

        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Subscription resumed — id: {}, nextBillingDate: {}", saved.getId(), nextBillingDate);
        return SubscriptionResponse.fromEntity(saved);
    }

    // ─── Private helper ──────────────────────────────────────────────────────
    private Subscription getAndVerifyOwnership(Long subscriptionId, Long userId) {
        Subscription subscription = subscriptionRepository
                .findByIdWithDetails(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + subscriptionId));

        if (!subscription.getUser().getId().equals(userId)) {
            log.warn("Ownership check failed — userId: {} does not own subscription id: {}", userId, subscriptionId);
            throw new UnauthorizedException("You don't have access to this subscription");
        }
        return subscription;
    }
}
