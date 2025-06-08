package order_service.microservice.service;
import order_service.microservice.repository.OutboxEventRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.shop.models.CartResponseDto;
import com.shop.models.OrderDto;
import order_service.microservice.events.OrderCreatedEvent;
import order_service.microservice.events.OrderCreatedEvent.OrderItemEvent;

import jakarta.transaction.Transactional;
import order_service.microservice.entity.Order;
import order_service.microservice.entity.OutboxEvent;
import order_service.microservice.repository.OrderRepository;

@Service
public class OrderServiceForDB {

    private final OrderServiceForEvents orderServiceForEvents;
    private final OutboxEventRepository outboxEventRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public OrderServiceForDB(OrderRepository orderRepository, 
                           OutboxEventRepository outboxEventRepository, 
                           OrderServiceForEvents orderServiceForEvents,
                           ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.orderServiceForEvents = orderServiceForEvents;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public OrderDto storeOrderInDBandOutbox(CartResponseDto cartDto) {
        //store in db 	
        Order order = Order.builder()
            .orderId(UUID.randomUUID().toString())
            .userId(cartDto.getUserId())
            .totalAmount(BigDecimal.valueOf(cartDto.getTotalItems()))
            .status("PENDING")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        orderRepository.save(order);
        	
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
            .orderId(order.getOrderId())
            .userId(order.getUserId())
            .totalAmount(order.getTotalAmount())
            .currency("USD")
            .timestamp(Instant.now())
            .items(cartDto.getItems().stream().map(item -> OrderItemEvent.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .priceAtOrderTime(BigDecimal.ZERO)
                .build()).collect(Collectors.toList()))
            .build();

        String eventPayloadJson;
        try {
            eventPayloadJson = objectMapper.writeValueAsString(orderCreatedEvent);
            System.out.println("DEBUG: JSON payload generated for Outbox: " + eventPayloadJson);
        } catch (JsonProcessingException e) {
            System.err.println("ERROR: Failed to serialize OrderCreatedEvent to JSON: " + e.getMessage());
            throw new RuntimeException("Error serializing event payload", e);
        }

        OutboxEvent outboxEvent = OutboxEvent.builder()
            .id(UUID.randomUUID().toString())
            .eventType("ORDER_CREATED")
            .aggregateId(order.getOrderId())
            .payload(eventPayloadJson)
            .status("PENDING")
            .createdAt(Instant.now())
            .retryCount(0)
            .build();

        outboxEventRepository.save(outboxEvent);

        orderServiceForEvents.sendPendingEventsToKafka();

        return OrderDto.builder()
            .orderId(order.getOrderId())
            .userId(order.getUserId())
            .totalAmount(order.getTotalAmount())
            .status(order.getStatus())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .build();
    }
}


