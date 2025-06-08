// package order_service.microservice.consumer;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.kafka.annotation.KafkaListener;
// // import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Component;
// import lombok.extern.slf4j.Slf4j;

// import order_service.microservice.events.InventoryReservedEventFail;
// import order_service.microservice.repository.OrderRepository;

// @Component
// @Slf4j
// public class InventoryFailEventConsumer {
//     @Autowired
//     private OrderRepository orderRepository;
//     // @Autowired
//     // private KafkaTemplate<String, String> kafkaTemplate;

//     @KafkaListener(topics = "inventorytest69-failure", groupId = "order-group")
//     public void consumeInventoryFailEvent(InventoryReservedEventFail event){
//        //remove from the order table 
//        //send a message to the user that the order failed
//        //send a message to the inventory service to add the items back to the inventory
//        String orderId = event.getorderId();
//        orderRepository.deleteByorderId(orderId);
//        //add later
//        //send event to notification service 
//        log.info("Order failed and removed from order table and inventory released , order number: {}", orderId);
//     }
    
// }
