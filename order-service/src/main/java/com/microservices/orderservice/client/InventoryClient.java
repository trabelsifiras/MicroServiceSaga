package com.microservices.orderservice.client;

import com.microservices.orderservice.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gateway-service", url = "${gateway.service.url:http://localhost:8080}", fallback = InventoryClientFallback.class)
public interface InventoryClient {
    
    @PostMapping("/api/inventory/reserve")
    InventoryResponse reserveInventory(@RequestBody InventoryRequest request);
    
    @PostMapping("/api/inventory/release")
    InventoryResponse releaseInventory(@RequestBody InventoryRequest request);
}