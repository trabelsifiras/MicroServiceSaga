package com.microservices.inventoryservice.kafka;

import com.microservices.inventoryservice.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    @KafkaListener(topics="order-micro",groupId = "order-group")
    public void listen(OrderResponse request){
        log.info(request.toString());
    }
}
