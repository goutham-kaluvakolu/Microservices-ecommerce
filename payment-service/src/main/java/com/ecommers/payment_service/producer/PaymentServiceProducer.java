package com.ecommers.payment_service.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.shop.events.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentServiceProducer {

    @Value("${kafka.topic.payment.events.publish.name}")
    private String paymentEventsTopicName;

    @Value("${kafka.topic.notification.events.publish.name}")
    private String notificationEventsTopicName;

    private final KafkaTemplate<String, BaseEvent> kafkaTemplate;

    public void sendPaymentSuccessEvent(BaseEvent event) {
        String kafkaTopic = paymentEventsTopicName;
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

    public void sendPaymentFailedEvent(BaseEvent event) {
        String kafkaTopic = paymentEventsTopicName;
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