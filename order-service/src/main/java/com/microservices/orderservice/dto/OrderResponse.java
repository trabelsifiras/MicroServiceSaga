package com.microservices.orderservice.dto;

import com.microservices.orderservice.model.OrderStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long id;
    private String productName;
    private Integer quantity;
    private Double price;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String message;
}