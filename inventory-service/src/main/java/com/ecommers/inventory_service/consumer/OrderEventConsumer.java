// src/main/java/com/ecommers/inventory_service/consumer/OrderEventConsumer.java
package com.ecommers.inventory_service.consumer; // Matches the folder structure

import com.ecommers.inventory_service.events.OrderCreatedEvent;
import com.ecommers.inventory_service.service.InventoryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.events.BaseEvent;
import com.shop.events.PaymentSuccessData;

@Service // Mark this class as a Spring service component
public class OrderEventConsumer {

    private final InventoryService inventoryService;

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);


    private final ObjectMapper objectMapper; // Inject ObjectMapper

    public OrderEventConsumer(ObjectMapper objectMapper, InventoryService inventoryService) {
        this.objectMapper = objectMapper;
        this.inventoryService = inventoryService;
    }
    
    @KafkaListener(topics = "${spring.kafka.consumer.order.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderCreatedEvent(ConsumerRecord<String, BaseEvent> record) {
        BaseEvent baseEvent = record.value();
        OrderCreatedEvent orderCreatedEvent = objectMapper.convertValue(baseEvent.getData(), OrderCreatedEvent.class);
        System.out.println("InventoryService: Received ORDER_CREATED for order: " + orderCreatedEvent.getOrderId());
        // Process order created
        inventoryService.handleOrderCreatedEvent(orderCreatedEvent);        
    }

    @KafkaListener(topics = "${spring.kafka.consumer.payment.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePaymentSuccessEvent(ConsumerRecord<String, BaseEvent> record) {
        BaseEvent baseEvent = record.value();
        PaymentSuccessData paymentSuccessData = objectMapper.convertValue(baseEvent.getData(), PaymentSuccessData.class);
        System.out.println("InventoryService: Received PAYMENT_SUCCESS for order: " + paymentSuccessData.getOrderId());
    }


    // You might also have other listener methods in this class or separate consumer classes
    // for other topics, e.g., payment-events if Inventory Service needs to react to payment failures.
    /*
    @KafkaListener(topics = "payment-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePaymentFailedEvent(PaymentFailedEvent event) {
        log.warn("Payment failed for Order ID: {}. Releasing inventory.", event.getOrderId());
        // Logic to release inventory
    }
    */
}