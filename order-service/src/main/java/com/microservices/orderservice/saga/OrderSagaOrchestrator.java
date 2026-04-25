package com.microservices.orderservice.saga;

import com.microservices.orderservice.client.InventoryClientWithResilience;
import com.microservices.orderservice.client.InventoryRequest;
import com.microservices.orderservice.dto.InventoryResponse;
import com.microservices.orderservice.dto.OrderRequest;
import com.microservices.orderservice.dto.OrderResponse;
import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.model.OrderStatus;
import com.microservices.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSagaOrchestrator {
    
    private final OrderRepository orderRepository;
    private final InventoryClientWithResilience inventoryClient;
    
    public OrderResponse executeSaga(OrderRequest request) {
        log.info("Starting Order Saga for product: {}", request.getProductName());
        
        Order order = new Order();
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setStatus(OrderStatus.PENDING);
        order = orderRepository.save(order);
        
        log.info("Order created with ID: {}", order.getId());
        
        try {
            order.setStatus(OrderStatus.ORDER_CREATED);
            order = orderRepository.save(order);
            log.info("Step 1: Order created, status: {}", order.getStatus());
            
            InventoryRequest inventoryRequest = new InventoryRequest();
            inventoryRequest.setProductName(request.getProductName());
            inventoryRequest.setQuantity(request.getQuantity());
            
            InventoryResponse inventoryResponse = inventoryClient.reserveInventory(inventoryRequest);
            
            if (!inventoryResponse.isSuccess()) {
                throw new RuntimeException("Inventory reservation failed: " + inventoryResponse.getMessage());
            }
            
            order.setStatus(OrderStatus.INVENTORY_RESERVED);
            order = orderRepository.save(order);
            log.info("Step 2: Inventory reserved, status: {}", order.getStatus());
            
            order.setStatus(OrderStatus.COMPLETED);
            order = orderRepository.save(order);
            log.info("Step 3: Order completed, status: {}", order.getStatus());
            
            return mapToResponse(order, "Order processed successfully!");
            
        } catch (Exception e) {
            log.error("Saga failed: {}", e.getMessage());
            compensate(order);
            order.setStatus(OrderStatus.FAILED);
            order = orderRepository.save(order);
            return mapToResponse(order, "Order failed: " + e.getMessage());
        }
    }
    
    private void compensate(Order order) {
        try {
            log.info("Executing compensation for order: {}", order.getId());
            InventoryRequest inventoryRequest = new InventoryRequest();
            inventoryRequest.setProductName(order.getProductName());
            inventoryRequest.setQuantity(order.getQuantity());
            inventoryClient.releaseInventory(inventoryRequest);
            log.info("Compensation: Inventory released for order: {}", order.getId());
        } catch (Exception e) {
            log.error("Compensation failed for order: {}. Manual intervention required.", order.getId());
        }
    }
    
    private OrderResponse mapToResponse(Order order, String message) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setProductName(order.getProductName());
        response.setQuantity(order.getQuantity());
        response.setPrice(order.getPrice());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setMessage(message);
        return response;
    }
}