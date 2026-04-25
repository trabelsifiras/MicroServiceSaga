package com.microservices.orderservice.client;

import com.microservices.orderservice.dto.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class InventoryClientFallback {

    @Value("${gateway.service.url:http://localhost:8080}")
    private String gatewayServiceUrl;

    private final RestTemplate restTemplate;

    public InventoryClientFallback(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public InventoryResponse reserveInventory(InventoryRequest request) {
        try {
            log.info("Fallback: Calling gateway at {}", gatewayServiceUrl);
            String url = gatewayServiceUrl + "/api/inventory/reserve";
            return restTemplate.postForObject(url, request, InventoryResponse.class);
        } catch (Exception e) {
            log.error("Fallback failed for reserve inventory: {}", e.getMessage());
            return new InventoryResponse(false, "Service unavailable: " + e.getMessage());
        }
    }

    public InventoryResponse releaseInventory(InventoryRequest request) {
        try {
            log.info("Fallback: Calling gateway at {}", gatewayServiceUrl);
            String url = gatewayServiceUrl + "/api/inventory/release";
            return restTemplate.postForObject(url, request, InventoryResponse.class);
        } catch (Exception e) {
            log.error("Fallback failed for release inventory: {}", e.getMessage());
            return new InventoryResponse(false, "Service unavailable: " + e.getMessage());
        }
    }
}