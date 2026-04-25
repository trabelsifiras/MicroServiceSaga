package com.microservices.orderservice.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String productName;
    private Integer quantity;
    private Double price;
}