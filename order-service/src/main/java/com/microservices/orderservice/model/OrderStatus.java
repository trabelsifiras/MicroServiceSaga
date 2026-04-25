package com.microservices.orderservice.model;

public enum OrderStatus {
    PENDING,
    ORDER_CREATED,
    INVENTORY_RESERVED,
    COMPLETED,
    CANCELLED,
    FAILED
}