package io.github.logith.billing_application.service;

import io.github.logith.billing_application.request.SubscriptionRequest;
import io.github.logith.billing_application.response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse subscribe(Long userId, SubscriptionRequest subscriptionRequest);
    SubscriptionResponse getById(Long subscriptionId, Long userId);
    List<SubscriptionResponse> getMySubscriptions(Long userId);
    SubscriptionResponse cancel(Long subscriptionId, Long userId);
    SubscriptionResponse pause(Long subscriptionId, Long userId);
    SubscriptionResponse resume(Long subscriptionId, Long userId);
}
