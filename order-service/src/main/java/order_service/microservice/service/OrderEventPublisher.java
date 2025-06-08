
// package order_service.microservice.service;


// import lombok.extern.slf4j.Slf4j; // For logging, if using Lombok
// import order_service.microservice.events.OrderCreatedEvent;

// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Service;


// /**
//  * Service responsible for publishing OrderCreatedEvent to Kafka.
//  */
// @Service
// @Slf4j // Lombok annotation for logger
// public class OrderEventPublisher {

//     private static final String ORDER_EVENTS_TOPIC = "ordertest69"; // Define your topic name

//     private final KafkaTemplate<String, Object> kafkaTemplate;

//     public OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
//         this.kafkaTemplate = kafkaTemplate;
//     }

//     /**
//      * Publishes an OrderCreatedEvent to the Kafka topic.
//      * Shipping address is excluded for now.
//      *
//      * @param orderId         The unique ID of the order.
//      * @param userId          The ID of the user who placed the order.
//      * @param totalAmount     The total amount of the order.
//      * @param currency        The currency of the order.
//      * @param items           List of order items.
//      */


//     // You might also add a method that takes the OrderCreatedEvent directly if it's already constructed
//     public void publishOrderCreatedEvent(OrderCreatedEvent event) {
//         log.info("Publishing pre-built OrderCreatedEvent for Order ID: {} to topic: {}", event.getOrderId(), ORDER_EVENTS_TOPIC);
//         kafkaTemplate.send(ORDER_EVENTS_TOPIC, event.getOrderId().toString(), event)
//             .whenComplete((result, ex) -> {
//                 if (ex == null) {
//                     log.info("Successfully published pre-built OrderCreatedEvent for Order ID: {} to topic: {} " +
//                              "with offset: {}", event.getOrderId(), ORDER_EVENTS_TOPIC, result.getRecordMetadata().offset());
//                 } else {
//                     log.error("Failed to publish pre-built OrderCreatedEvent for Order ID: {} to topic: {}. Error: {}",
//                             event.getOrderId(), ORDER_EVENTS_TOPIC, ex.getMessage(), ex);
//                 }
//             });
//     }
// }


    // src/main/java/com/example/orderservice/outbox/OutboxEventPublisher.java
    package order_service.microservice.service;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Component;

    /**
     * A scheduled component responsible for polling the Outbox table
     * and publishing pending events to Kafka.
     */
    @Component
    public class OrderEventPublisher {

        private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

        private final OrderServiceForEvents orderServiceForEvents;

        public OrderEventPublisher(OrderServiceForEvents orderServiceForEvents) {
            this.orderServiceForEvents = orderServiceForEvents;
        }

        /**
         * This method is scheduled to run periodically to send pending events.
         * The 'fixedRate' property runs the task at a fixed interval
         * regardless of the previous task's completion.
         * 'fixedDelay' would wait for the previous task to finish before starting the timer.
         * Using 'cron' can give more precise scheduling control.
         *
         * @Scheduled(fixedRate = 5000) // Runs every 5 seconds
         * @Scheduled(fixedDelay = 5000) // Waits 5 seconds after previous run finishes
         * @Scheduled(cron = "0/5 * * * * ?") // Runs every 5 seconds on the second
         */
        @Scheduled(fixedRate = 5000) // Run every 5 seconds to check for pending events
        public void publishPendingOutboxEvents() {
            log.info("Scheduled task: Checking for pending outbox events...");
            orderServiceForEvents.sendPendingEventsToKafka();
        }
    }
    