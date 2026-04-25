package com.microservices.inventoryservice.dto;

public enum OrderStatus {
    PENDING,
    ORDER_CREATED,
    INVENTORY_RESERVED,
    COMPLETED,
    CANCELLED,
    FAILED
}