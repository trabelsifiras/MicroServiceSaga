package com.microservices.orderservice.client;

import com.microservices.orderservice.dto.InventoryResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryClientWithResilience {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final InventoryClientFallback fallback;

    public InventoryResponse reserveInventory(InventoryRequest request) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("inventoryService");
        Retry retry = retryRegistry.retry("inventoryService");

        Supplier<InventoryResponse> retrySupplier = Retry.decorateSupplier(retry, () -> {
            log.info("Calling inventory service to reserve inventory...");
            return fallback.reserveInventory(request);
        });

        Supplier<InventoryResponse> circuitBreakerSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, retrySupplier);

        return circuitBreakerSupplier.get();
    }

    public InventoryResponse releaseInventory(InventoryRequest request) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("inventoryService");
        Retry retry = retryRegistry.retry("inventoryService");

        Supplier<InventoryResponse> retrySupplier = Retry.decorateSupplier(retry, () -> {
            log.info("Calling inventory service to release inventory...");
            return fallback.releaseInventory(request);
        });

        Supplier<InventoryResponse> circuitBreakerSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, retrySupplier);

        return circuitBreakerSupplier.get();
    }
}