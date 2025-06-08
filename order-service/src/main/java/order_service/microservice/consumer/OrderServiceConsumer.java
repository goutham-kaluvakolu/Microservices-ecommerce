package order_service.microservice.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.events.BaseEvent;
import com.shop.events.PaymentSuccessData;
import com.shop.events.InventoryLockedData;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Component
public class OrderServiceConsumer {

    private final ObjectMapper objectMapper; // Inject ObjectMapper

    public OrderServiceConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment_topic", groupId = "order-service-group")
    public void consumePaymentEvent(ConsumerRecord<String, BaseEvent> record) {
        BaseEvent baseEvent = record.value();
        if ("PAYMENT_SUCCESS".equals(baseEvent.getEventType())) {
            // Manually deserialize the 'data' part to the specific type
            PaymentSuccessData paymentData = objectMapper.convertValue(baseEvent.getData(), PaymentSuccessData.class);
            System.out.println("OrderService: Received PAYMENT_SUCCESS for order: " + paymentData.getOrderId());
            // Process payment success
        } else {
            System.out.println("OrderService: Received unexpected event type from payment topic: " + baseEvent.getEventType());
        }
    }

    @KafkaListener(topics = "inventory_topic", groupId = "order-service-group")
    public void consumeInventoryEvent(ConsumerRecord<String, BaseEvent> record) {
        BaseEvent baseEvent = record.value();
        if ("INVENTORY_LOCKED".equals(baseEvent.getEventType())) {
            // Manually deserialize the 'data' part to the specific type
            InventoryLockedData inventoryData = objectMapper.convertValue(baseEvent.getData(), InventoryLockedData.class);
            System.out.println("OrderService: Received INVENTORY_LOCKED for order: " + inventoryData.getOrderId());
            // Process inventory locked
        } else {
             System.out.println("OrderService: Received unexpected event type from inventory topic: " + baseEvent.getEventType());
        }
    }

    // If you were subscribing to multiple topics with one listener method,
    // you would use the 'topics' array:
    // @KafkaListener(topics = {"payment", "inventory"}, groupId = "order-service-group")
    // public void consumeEvents(ConsumerRecord<String, BaseEvent> record) {
    //     BaseEvent baseEvent = record.value();
    //     switch (baseEvent.getEventType()) {
    //         case "PAYMENT_SUCCESS":
    //             PaymentSuccessData paymentData = objectMapper.convertValue(baseEvent.getData(), PaymentSuccessData.class);
    //             // ... process
    //             break;
    //         case "INVENTORY_LOCKED":
    //             InventoryLockedData inventoryData = objectMapper.convertValue(baseEvent.getData(), InventoryLockedData.class);
    //             // ... process
    //             break;
    //         default:
    //             System.out.println("Unknown event type: " + baseEvent.getEventType());
    //     }
    // }
}