// src/main/java/com/example/orderservice/outbox/OutboxService.java
package order_service.microservice.service;

import org.springframework.kafka.core.KafkaTemplate;

import order_service.microservice.entity.OutboxEvent;
import order_service.microservice.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shop.events.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

/**
 * Service class for managing Outbox Events.
 * Provides methods for publishing events to the outbox table within a transaction,
 * and for sending pending events to a Kafka queue.
 */
@Service
public class OrderServiceForEvents {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceForEvents.class);

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, BaseEvent> kafkaTemplate; // Inject KafkaTemplate
    // private final ObjectMapper objectMapper; // Inject ObjectMapper for potential payload re-serialization

    public OrderServiceForEvents(OutboxEventRepository outboxEventRepository, KafkaTemplate<String, BaseEvent> kafkaTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        // this.objectMapper = objectMapper;
    }

    // Define status constants to avoid magic strings, even if not using an enum
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_ACKNOWLEDGED = "ACKNOWLEDGED";
    public static final String STATUS_FAILED = "FAILED";

    /**
     * Finds and retrieves pending outbox events for publishing.
     * This method is typically called by a separate scheduler/poller.
     *
     * @param limit The maximum number of pending events to retrieve.
     * @return A list of pending OutboxEvent entities.
     */
    @Transactional(readOnly = true)
    public List<OutboxEvent> getPendingEvents(int limit) {
        // Log the retrieval of pending events
        List<OutboxEvent> pendingEvents = outboxEventRepository.findEventsByStatusWithLimit(STATUS_PENDING, limit);
        log.debug("Found {} pending outbox events.", pendingEvents.size());
        return pendingEvents;
    }

    /**
     * Attempts to send pending outbox events to a Kafka topic.
     * This method is typically called by a scheduled task.
     * It handles sending, status updates, and basic error logging.
     */
    @Transactional // Ensure the status update is transactional
    public void sendPendingEventsToKafka() {
        log.info("Attempting to send pending outbox events to Kafka...");
        // Fetch a batch of pending events. You might want to configure this limit.
        List<OutboxEvent> pendingEvents = getPendingEvents(100); // Fetch up to 100 events at a time

        for (OutboxEvent event : pendingEvents) {
            try {
                // Determine the Kafka topic. You might use eventType or a mapping.

                String kafkaTopic =  event.getEventType().toLowerCase();
                if(kafkaTopic.equals("order_created")){
                    kafkaTopic = "order_topic";
                }else if(kafkaTopic.equals("inventory_reserved")){
                    kafkaTopic = "inventory_topic";
                }else if(kafkaTopic.equals("payment_success")){
                    kafkaTopic = "payment_topic";
                }else if(kafkaTopic.equals("notification_sent")){
                    kafkaTopic = "notification_topic";
                }

                log.debug("Sending event ID: {} to topic: {}", event.getId(), kafkaTopic);
                //convert the event to a base event
                BaseEvent baseEvent = new BaseEvent();
                baseEvent.setEventId(event.getAggregateId());
                baseEvent.setEventType(event.getEventType());
                baseEvent.setEventVersion("1.0");
                baseEvent.setTimestamp(event.getCreatedAt().toString());
                baseEvent.setCorrelationId(event.getCorrelationId());
                baseEvent.setSource("order-service");
                baseEvent.setData(event.getPayload());
                baseEvent.setMetadata(null);

                
                // Send the payload to Kafka. Use event.getId() as the key for partitioning.
                kafkaTemplate.send(kafkaTopic, baseEvent.getEventId(), baseEvent)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            // Successfully sent to Kafka, now update status in DB
                            log.info("Successfully sent event ID: {} to Kafka. Updating status to SENT.", event.getId());
                            markEventAsSent(event.getId());
                        } else {
                            // Failed to send to Kafka
                            log.error("Failed to send event ID: {} to Kafka: {}", event.getId(), ex.getMessage());
                            // Depending on retry strategy, you might increment retryCount or mark as FAILED
                            // For simplicity, we'll mark as FAILED after one attempt here,
                            // but a more robust solution would track retries and eventually fail.
                            markEventAsFailed(event.getId());
                        }
                    });
            } catch (Exception e) {
                log.error("Unexpected error while preparing/sending event ID: {}: {}", event.getId(), e.getMessage(), e);
                // Mark as failed if an exception occurs during sending setup
                markEventAsFailed(event.getId());
            }
        }
        log.info("Finished processing pending outbox events for this batch.");
    }


    /**
     * Marks an outbox event as SENT after successful publication to a message broker.
     *
     * @param eventId The ID of the event to mark as sent.
     */
    @Transactional
    public void markEventAsSent(String eventId) {
        outboxEventRepository.findById(eventId).ifPresent(event -> {
            event.setStatus(STATUS_SENT); // Using constant
            event.setRetryCount(event.getRetryCount() + 1); // Increment retry count
            outboxEventRepository.save(event);
            log.debug("Event ID: {} status updated to SENT.", event.getId());
        });
    }

    // /**
    //  * Marks an outbox event as ACKNOWLEDGED after processing by a consumer.
    //  *
    //  * @param eventId The ID of the event to mark as acknowledged.
    //  * @param serviceName The name of the service acknowledging the event.
    //  */
    // @Transactional
    // public void markEventAsAcknowledged(String eventId, String serviceName) {
    //     outboxEventRepository.findById(eventId).ifPresent(event -> {
    //         List<String> receivedAcks = event.getReceivedAcknowledgmentsJson();
    //         if (!receivedAcks.contains(serviceName)) {
    //             receivedAcks.add(serviceName);
    //             event.setReceivedAcknowledgmentsJson(receivedAcks);
    //             log.debug("Event ID: {} received acknowledgment from: {}", event.getId(), serviceName);
    //         }

    //         // If all expected acknowledgments are received, mark as ACKNOWLEDGED
    //         if (event.getExpectedAcknowledgments() != null &&
    //             event.getExpectedAcknowledgments().size() > 0 &&
    //             receivedAcks.containsAll(event.getExpectedAcknowledgments())) {
    //             event.setStatus(STATUS_ACKNOWLEDGED); // Using constant
    //             log.info("Event ID: {} fully ACKNOWLEDGED.", event.getId());
    //         } else if (event.getExpectedAcknowledgments() == null || event.getExpectedAcknowledgments().isEmpty()) {
    //              // If no specific acknowledgments were expected, mark as ACKNOWLEDGED immediately upon first ack
    //              event.setStatus(STATUS_ACKNOWLEDGED); // Using constant
    //              log.info("Event ID: {} ACKNOWLEDGED (no specific expected acknowledgments).", event.getId());
    //         }

    //         outboxEventRepository.save(event);
    //     });
    // }

    /**
     * Marks an outbox event as FAILED, typically after exhausting retry attempts or initial send failure.
     *
     * @param eventId The ID of the event to mark as failed.
     */
    @Transactional
    public void markEventAsFailed(String eventId) {
        outboxEventRepository.findById(eventId).ifPresent(event -> {
            event.setStatus(STATUS_FAILED); // Using constant
            event.setRetryCount(event.getRetryCount() + 1); // Also increment on failure
            outboxEventRepository.save(event);
            log.error("Event ID: {} status updated to FAILED.", event.getId());
        });
    }
}
