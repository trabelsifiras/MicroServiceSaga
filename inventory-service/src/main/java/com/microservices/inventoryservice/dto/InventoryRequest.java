package com.microservices.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryRequest {
    private String productName;
    private Integer quantity;
}