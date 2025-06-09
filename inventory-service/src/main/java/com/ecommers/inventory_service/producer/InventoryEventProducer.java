package com.ecommers.inventory_service.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.shop.events.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventProducer {

    @Value("${kafka.topic.inventory.events.publish.name}")
    private String inventoryEventsTopicName;

    private final KafkaTemplate<String, BaseEvent> kafkaTemplate;

    public void sendInventoryReservedEventSuccess(BaseEvent event) {
        String kafkaTopic = "inventory_topic";
        log.debug("Sending event ID: {} to topic: {}", event.getEventId(), kafkaTopic);

        kafkaTemplate.send(kafkaTopic, event.getEventId(), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully sent event ID: {} to Kafka with offset: {}", 
                        event.getEventId(), result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send event ID: {} to Kafka: {}", 
                        event.getEventId(), ex.getMessage());
                }
            });
    }

    public void sendInventoryReservationFailedEvent(BaseEvent event) {
        String kafkaTopic = "inventory_topic";
        log.debug("Sending failed event ID: {} to topic: {}", event.getEventId(), kafkaTopic);

        kafkaTemplate.send(kafkaTopic, event.getEventId(), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully sent failed event ID: {} to Kafka with offset: {}", 
                        event.getEventId(), result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send failed event ID: {} to Kafka: {}", 
                        event.getEventId(), ex.getMessage());
                }
            });
    }
}