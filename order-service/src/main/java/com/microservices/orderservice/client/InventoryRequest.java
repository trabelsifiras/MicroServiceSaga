package com.microservices.orderservice.client;

import lombok.Data;

@Data
public class InventoryRequest {
    private String productName;
    private Integer quantity;
}