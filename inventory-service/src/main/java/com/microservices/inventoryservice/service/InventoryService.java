package com.microservices.inventoryservice.service;

import com.microservices.inventoryservice.dto.InventoryRequest;
import com.microservices.inventoryservice.dto.InventoryResponse;
import com.microservices.inventoryservice.model.Inventory;
import com.microservices.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    
    @Transactional
    public InventoryResponse reserveInventory(InventoryRequest request) {
        log.info("Reserving inventory for product: {}, quantity: {}", 
                request.getProductName(), request.getQuantity());
        
        Inventory inventory = inventoryRepository.findByProductName(request.getProductName())
                .orElse(null);
        
        if (inventory == null) {
            log.warn("Product not found: {}", request.getProductName());
            return new InventoryResponse(false, "Product not found: " + request.getProductName());
        }
        
        if (!inventory.getAvailable()) {
            log.warn("Product not available: {}", request.getProductName());
            return new InventoryResponse(false, "Product not available");
        }
        
        if (inventory.getQuantity() < request.getQuantity()) {
            log.warn("Insufficient inventory for product: {}. Available: {}, Requested: {}", 
                    request.getProductName(), inventory.getQuantity(), request.getQuantity());
            return new InventoryResponse(false, "Insufficient inventory. Available: " + inventory.getQuantity());
        }
        
        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        inventoryRepository.save(inventory);
        
        log.info("Inventory reserved successfully for product: {}", request.getProductName());
        return new InventoryResponse(true, "Inventory reserved successfully");
    }
    
    @Transactional
    public InventoryResponse releaseInventory(InventoryRequest request) {
        log.info("Releasing inventory for product: {}, quantity: {}", 
                request.getProductName(), request.getQuantity());
        
        Inventory inventory = inventoryRepository.findByProductName(request.getProductName())
                .orElse(null);
        
        if (inventory == null) {
            log.warn("Product not found: {}", request.getProductName());
            return new InventoryResponse(false, "Product not found: " + request.getProductName());
        }
        
        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        inventoryRepository.save(inventory);
        
        log.info("Inventory released successfully for product: {}", request.getProductName());
        return new InventoryResponse(true, "Inventory released successfully");
    }
    
    public boolean isAvailable(String productName, Integer quantity) {
        return inventoryRepository.findByProductName(productName)
                .map(inv -> inv.getAvailable() && inv.getQuantity() >= quantity)
                .orElse(false);
    }


}