package order_service.microservice.service;

import com.shop.models.CartResponseDto;
// import order_service.microservice.dto.OrderDto;
// import order_service.microservice.dto.OrderItemDto;
import com.shop.models.OrderDto;
import com.shop.models.OrderItemDto;
import order_service.microservice.entity.Order;
import order_service.microservice.entity.OrderItem;
import order_service.microservice.repository.OrderRepository;
import order_service.microservice.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderServiceForDB orderServiceForDB;
    private final OrderServiceForEvents orderServiceForEvents;
    private final WebClient.Builder webClientBuilder;
    // private final OrderEventPublisher orderEventPublisher;

    // public OrderService(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
    //     this.orderRepository = orderRepository;
    //     this.orderEventPublisher = orderEventPublisher;
    // }
    public OrderService(OrderRepository orderRepository, OrderServiceForDB orderServiceForDB, OrderServiceForEvents orderServiceForEvents, WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.orderServiceForDB = orderServiceForDB;
        this.orderServiceForEvents = orderServiceForEvents;
        this.webClientBuilder = webClientBuilder;
        // this.orderEventPublisher = orderEventPublisher;
    }

    // @Transactional
    // public Order placeOrder(OrderDto orderDTO) {
    //     List<OrderCreatedEvent.OrderItemEvent> orderItemsForEvent = orderDTO.getItems().stream()
    //         .map(itemDto -> OrderCreatedEvent.OrderItemEvent.builder()
    //             .productId(itemDto.getProductId())
    //             .quantity(itemDto.getQuantity())
    //             .priceAtOrderTime(itemDto.getProductPrice())
    //             .build())
    //         .collect(Collectors.toList());

    //     Order order = Order.builder()
    //             .orderId(UUID.randomUUID().toString())
    //             .userId(Long.valueOf(orderDTO.getUserId()))
    //             .status("PENDING")
    //             .build();

    //     // Add order items
    //     for (OrderItemDto itemDto : orderDTO.getItems()) {
    //         OrderItem item = OrderItem.builder()
    //                 .productId(itemDto.getProductId())
    //                 .productName(itemDto.getProductName())
    //                 .productPrice(itemDto.getProductPrice())
    //                 .quantity(itemDto.getQuantity())
    //                 .build();
    //         order.addOrderItem(item);
    //     }

    //     // Calculate total
    //     BigDecimal total = order.getItems().stream()
    //             .map(i -> i.getProductPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
    //             .reduce(BigDecimal.ZERO, BigDecimal::add);
    //     order.setTotalAmount(total);
    //     order.setStatus("PAID");

    //     try{
    //         Order savedOrder = orderRepository.save(order);

    //         // orderEventPublisher.publishOrderCreatedEvent(
    //         //     OrderCreatedEvent.builder()
    //         //         .orderId(savedOrder.getId())
    //         //         .userId(savedOrder.getUserId())
    //         //         .totalAmount(savedOrder.gettotalAmount())
    //         //         .currency("USD")
    //         //         .timestamp(Instant.now())
    //         //         .items(orderItemsForEvent)
    //         //         .build()
    //         // );
    
    //         log.info("Order placed and event published for Order ID: {} by User ID: {}", 
    //                 savedOrder.getOrderId(), savedOrder.getUserId());
           
    //                 return savedOrder;
            
    //     }

    //     catch(Exception e){
    //         log.error("Error placing order: {}", e.getMessage());
            
    //         throw new RuntimeException("Error placing order", e);
    //     }
    // }
    @Transactional
    public OrderDto placeOrder(String userId) {
        //get the user cart
        WebClient webClient = webClientBuilder.build();
        String cartUrl = "http://localhost:8085/api/v1/cart/get/internal/" + userId;
        CartResponseDto cartDto = webClient.get()
        .uri(cartUrl)
        .retrieve()
        .bodyToMono(CartResponseDto.class)
        .block();
        if(cartDto == null){
            throw new RuntimeException("Cart not found");
        }
        //check if the cart is empty
        if(cartDto.getItems().isEmpty()){
            throw new RuntimeException("Cart is empty");
        }
        //store in db 
        //store in outbx
        OrderDto orderDto = orderServiceForDB.storeOrderInDBandOutbox(cartDto);
        //publish event
        // orderServiceForEvents.sendPendingEventsToKafka();
        return orderDto;

    }

    public List<OrderDto> getOrders() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrders'");
    }
}