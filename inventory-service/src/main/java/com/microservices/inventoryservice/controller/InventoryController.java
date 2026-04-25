package com.microservices.inventoryservice.controller;

import com.microservices.inventoryservice.dto.InventoryRequest;
import com.microservices.inventoryservice.dto.InventoryResponse;
import com.microservices.inventoryservice.model.Inventory;
import com.microservices.inventoryservice.repository.InventoryRepository;
import com.microservices.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;
    
    @PostMapping("/reserve")
    public ResponseEntity<InventoryResponse> reserveInventory(@RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.reserveInventory(request));
    }
    
    @PostMapping("/release")
    public ResponseEntity<InventoryResponse> releaseInventory(@RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.releaseInventory(request));
    }
    
    @GetMapping("/flux")
    public Flux<Inventory> getAllInventoryFlux() {
        return Flux.fromIterable(inventoryRepository.findAll());
    }
    
    @PostMapping
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryRepository.save(inventory));
    }
}