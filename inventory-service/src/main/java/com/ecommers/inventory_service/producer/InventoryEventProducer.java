// package com.ecommers.inventory_service.producer;

// import com.ecommers.inventory_service.events.InventoryReservedEventSuccess;
// import com.ecommers.inventory_service.events.InventoryReservedEventFail; // Import the new failure event

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Component;

// @Component
// public class InventoryEventProducer {

//     @Value("${kafka.topic.inventory.events.name}")
//     private String inventoryEventsTopicName; // Topic for successful reservation events

//     // New: Inject the topic name for failure events from application.properties
//     @Value("${kafka.topic.inventory.failure.name}")
//     private String inventoryFailureTopicName; // Topic for failed reservation events

//     private final KafkaTemplate<String, Object> kafkaTemplate;

//     public InventoryEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
//         this.kafkaTemplate = kafkaTemplate;
//     }

//     /**
//      * Sends an InventoryReservedEventSuccess to the Kafka topic.
//      * This is for the happy path (inventory successfully reserved).
//      * @param event The InventoryReservedEventSuccess to send.
//      */
//     public void sendInventoryReservedEventSuccess(InventoryReservedEventSuccess event) {
//         String key = event.getorderId(); // Use orderId as the message key

//         kafkaTemplate.send(inventoryEventsTopicName, key, event)
//             .whenComplete((result, ex) -> {
//                 if (ex == null) {
//                     System.out.println("InventoryReservedEventSuccess sent successfully to topic: " + inventoryEventsTopicName +
//                                        " with offset: " + result.getRecordMetadata().offset() +
//                                        " for orderId: " + event.getorderId() +
//                                        " | Status: " + event.getReservationStatus());
//                 } else {
//                     System.err.println("Failed to send InventoryReservedEventSuccess for orderId: " + event.getorderId() +
//                                        " due to: " + ex.getMessage());
//                 }
//             });
//     }

//     /**
//      * Sends an InventoryReservationFailedEvent to a dedicated Kafka topic.
//      * This is for handling inventory reservation failures.
//      * @param event The InventoryReservationFailedEvent to send.
//      */
//     public void sendInventoryReservationFailedEvent(InventoryReservedEventFail event) {
//         String key = String.valueOf(event.getorderId()); // Use orderId as the message key

//         kafkaTemplate.send(inventoryFailureTopicName, key, event)
//             .whenComplete((result, ex) -> {
//                 if (ex == null) {
//                     System.out.println("InventoryReservationFailedEvent sent successfully to topic: " + inventoryFailureTopicName +
//                                        " with offset: " + result.getRecordMetadata().offset() +
//                                        " for orderId: " + event.getorderId());
//                 } else {
//                     System.err.println("Failed to send InventoryReservationFailedEvent for orderId: " + event.getorderId() +
//                                        " due to: " + ex.getMessage());
//                 }
//             });
//     }
// }