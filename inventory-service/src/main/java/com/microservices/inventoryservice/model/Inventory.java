package com.microservices.inventoryservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String productName;
    
    private Integer quantity;
    
    private Boolean available;
    
    @PrePersist
    protected void onCreate() {
        if (available == null) {
            available = true;
        }
    }
}