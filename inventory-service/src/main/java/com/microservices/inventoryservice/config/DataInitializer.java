package com.microservices.inventoryservice.config;

import com.microservices.inventoryservice.model.Inventory;
import com.microservices.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final InventoryRepository inventoryRepository;
    
    @Override
    public void run(String... args) {
        if (inventoryRepository.count() == 0) {
            log.info("Initializing inventory data...");
            
            Inventory inv1 = new Inventory();
            inv1.setProductName("Laptop");
            inv1.setQuantity(100);
            inv1.setAvailable(true);
            inventoryRepository.save(inv1);
            
            Inventory inv2 = new Inventory();
            inv2.setProductName("Phone");
            inv2.setQuantity(200);
            inv2.setAvailable(true);
            inventoryRepository.save(inv2);
            
            Inventory inv3 = new Inventory();
            inv3.setProductName("Tablet");
            inv3.setQuantity(150);
            inv3.setAvailable(true);
            inventoryRepository.save(inv3);
            
            log.info("Inventory data initialized successfully");
        }
    }
}