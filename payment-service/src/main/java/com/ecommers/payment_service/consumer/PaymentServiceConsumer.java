package com.ecommers.payment_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.events.BaseEvent;
import com.shop.events.InventoryReservedEventSuccess;
import com.shop.events.PaymentSuccessData;
import com.ecommers.payment_service.producer.PaymentServiceProducer;

import java.time.Instant;
import java.util.UUID;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

@Component
public class PaymentServiceConsumer {

    private final ObjectMapper objectMapper; // Inject ObjectMapper
    private final Logger log = LoggerFactory.getLogger(PaymentServiceConsumer.class);
    
    private final PaymentServiceProducer paymentServiceProducer;

    public PaymentServiceConsumer(ObjectMapper objectMapper, PaymentServiceProducer paymentServiceProducer) {
        this.objectMapper = objectMapper;
        this.paymentServiceProducer = paymentServiceProducer;
    }

    @KafkaListener(topics = "inventory_topic", groupId = "payment-service-group")
    public void consumeInventoryEvent(ConsumerRecord<String, BaseEvent> record) {
        BaseEvent baseEvent = record.value();
        if ("INVENTORY_RESERVED".equals(baseEvent.getEventType())) {
            // Manually deserialize the 'data' part to the specific type
            InventoryReservedEventSuccess inventoryData = objectMapper.convertValue(baseEvent.getData(),InventoryReservedEventSuccess.class);
            System.out.println("Payment service received: INVENTORY_RESERVED for order: " + inventoryData.getOrderId());
            // Process inventory locked
            
            // private String orderId;
            // private String transactionId;
            // private double amount;
            // private String currency;
            // private String paymentMethod;
            // private String customerId;
            // private String status;
            PaymentSuccessData paymentSuccessData = PaymentSuccessData.builder()
            .orderId(inventoryData.getOrderId())
            .transactionId(UUID.randomUUID().toString())
            .amount(inventoryData.getReservedItems().stream()
                .mapToDouble(item -> Optional.ofNullable(item.getPriceAtOrderTime()).orElse(1.0) * item.getReservedQuantity())
                .sum())
            .currency("USD")
            .paymentMethod("CREDIT_CARD")
            .customerId(inventoryData.getUserId())
            .status("SUCCESS")
            .build();

            BaseEvent paymentSuccessEvent = BaseEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .eventType("PAYMENT_SUCCESS")
            .eventVersion("1.0")
            .timestamp(Instant.now().toString())
            .correlationId(baseEvent.getCorrelationId())
            .source("payment-service")
            .data(paymentSuccessData)
            .build();

            paymentServiceProducer.sendPaymentSuccessEvent(paymentSuccessEvent);
        } else if("INVENTORY_LOCKED".equals(baseEvent.getEventType())){
            System.out.println("Payment service received: INVENTORY_LOCKED for order: ");
            // Process inventory locked
            paymentServiceProducer.sendPaymentFailedEvent(baseEvent);
        } else {
            System.out.println("OrderService: Received unexpected event type from inventory topic: " + baseEvent.getEventType());
        }
    }

}