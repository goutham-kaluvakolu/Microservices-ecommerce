// src/main/java/com/ecommers/inventory_service/consumer/OrderEventConsumer.java
package com.ecommers.inventory_service.consumer; // Matches the folder structure

import com.ecommers.inventory_service.events.OrderCreatedEvent; // Import from your events package
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommers.inventory_service.service.InventoryService;

@Service // Mark this class as a Spring service component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @Autowired
    private InventoryService inventoryService;

    // This method will listen to the "order-events" topic
    @KafkaListener(topics = "${kafka.topic.order-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for Order ID: {} by User ID: {}", 
            event.getOrderId(), event.getUserId());
        
        try {
            inventoryService.handleOrderCreatedEvent(event);
            log.info("Successfully processed order event for Order ID: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("Error processing order event for Order ID: {}. Error: {}", 
                event.getOrderId(), e.getMessage());
            // Here you might want to implement retry logic or dead letter queue handling
            throw e;
        }
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